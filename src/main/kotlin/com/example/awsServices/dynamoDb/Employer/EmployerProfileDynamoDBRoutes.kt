package com.example.awsServices.dynamoDb

import com.example.Data.models.Auth.AuthRequest
import com.example.Data.models.Auth.AuthResponse
import com.example.Data.models.SupervisorProfileDynamoDBInterface
import com.example.Data.models.workerVisualiser.Personal
import com.example.Data.models.workerVisualiser.WorkerSite
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

/*fun Route.authoriseUser(
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
}*/

//aws visualiser route functions for Supervisors
// putSupervisorSignupInfo
// putSupervisorSiteInfo
// putSupervisorPersonalData

fun Route.putSupervisorSignupInfo(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
    hashingService : HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("putSupervisorSignupInfo") {
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

        val wasAcknowledged = SupervisorDataSource.putSupervisorSignupInfo(
            email = request.email,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

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

fun Route.putSupervisorSiteInfo(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
) {
    post("putSupervisorSiteInfo") {
        val request = call.receiveOrNull<WorkerSite>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        SupervisorDataSource.putSupervisorSiteInfo(
            email = request.email,
            address = request.address,
            siteExpliation = request.siteExplanation,
            siteAddressExplination = request.siteAddressExplanation,
            googleMapsLocation = request.googleMapsLocation,
            siteDaysWorkedAndThereUsualStartAndEndTime = request.siteDaysWorkedAndThereUsualStartAndEndTime,
            terrain = request.terrain,
            sitePhoto = request.sitePhoto
        )
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.putSupervisorPersonalData(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
) {
    post("putSupervisorPersonalData") {
        val request = call.receiveOrNull<Personal>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        SupervisorDataSource.putSupervisorPersonalData(
            email = request.email,
            supervisor = request.supervisor,
            firstname = request.firstname,
            lastname = request.lastname,
            recordOfAttendance = request.recordOfAttendance,
            rate = request.rate,
            personalPhoto = request.personalPhoto
        )
        call.respond(HttpStatusCode.OK)
    }
}





