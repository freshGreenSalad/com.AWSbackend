package com.example.awsServices.dynamoDb

import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.AuthRequest
import com.example.Data.models.AuthResponse
import com.example.Data.models.SupervisorProfileDynamoDBInterface
import com.example.Data.models.workerVisualiser.SpecialLicence
import com.plcoding.security.hashing.HashingService
import com.plcoding.security.hashing.SaltedHash
import com.plcoding.security.token.TokenClaim
import com.plcoding.security.token.TokenConfig
import com.plcoding.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.codec.digest.DigestUtils

fun Route.PutProflieInDynamoDB(
    profileDataSource: SupervisorProfileDynamoDBInterface,
    hashingService : HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
){
    post("UserAuthSignUp") {

        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val areFieldsBlank = request.email.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 8
        if(areFieldsBlank || isPwTooShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)

        val user = Profile(
            email = request.email,
            password = saltedHash.hash,
            salt = saltedHash.salt,
            firstName = "",
            lastName = "",
            company = "",
        )
        val wasAcknowledged = profileDataSource.insertUser(user)

        if(!wasAcknowledged)  {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = request.email
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }
}

fun Route.authoriseUser(
    profileDataSource: SupervisorProfileDynamoDBInterface,
    hashingService : HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
){
    post("signIn") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val profile = profileDataSource.getProfileByEmail(request.email)
        if (profile == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = profile.password,
                salt = profile.salt
            )
        )
        if (!isValidPassword) {
            println("Entered hash: ${DigestUtils.sha256Hex("${profile.salt}${request.password}")}, Hashed PW: ${profile.password}")
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = profile.email
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }
}
fun Route.testGetProfileFromDynamodb(
    profileDataSource: SupervisorProfileDynamoDBInterface,
){
    post("signinTest") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val profile = profileDataSource.getProfileByEmail(request.email)
        call.respond(HttpStatusCode.OK)
    }
}

//aws visualiser route functions for Supervisors
// putSupervisorSignupInfo
// putSupervisorSiteInfo
// putSupervisorPersonalData

fun Route.putWorkerSpecialLicence(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface
) {
    post("postEmailPassword") {
        val request = call.receiveOrNull<SpecialLicence>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        SupervisorDataSource.putSupervisorSignupInfo(
            email = request.email,
            licenceType = request.licenceType,
            issueDate = request.issueDate,
            expireyDate = request.expiryDate,
            licencePhoto = request.licencePhoto
        )
        call.respond(HttpStatusCode.OK)
    }
}






