package com.example.awsServices.dynamoDb.employer

import com.example.Data.models.Auth.AuthRequest
import com.example.Data.models.Auth.AuthResponse
import com.example.Data.models.SupervisorProfileDynamoDBInterface
import com.example.Data.models.supervisorVisualiser.SupervisorProfile
import com.example.Data.models.supervisorVisualiser.SupervisorSite
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

//aws visualiser route functions for Supervisors
// putSupervisorSignupInfo
// putSupervisorSiteInfo
// putSupervisorPersonalData

fun Route.putSupervisorSignupInfo(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("putSupervisorSignupInfo") {
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val areFieldsBlank = request.email.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 8
        if (areFieldsBlank || isPwTooShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)

        val wasAcknowledged = SupervisorDataSource.putSupervisorSignupInfo(
            email = request.email,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        if (!wasAcknowledged) {
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
                token = token,
                isSupervisor = true

            )
        )
    }
}

fun Route.putSupervisorSiteInfo(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
) {
    post("putSupervisorSiteInfo") {
        val request = kotlin.runCatching { call.receiveNullable<SupervisorSite>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        SupervisorDataSource.putSupervisorSiteInfo(
            email = request.email,
            address = request.address,
            //siteExpliation = request.siteExplanation,
            //siteAddressExplination = request.siteAddressExplanation,
            googleMapsLocation = request.location,
            //siteDaysWorkedAndThereUsualStartAndEndTime = request.siteDaysWorkedAndThereUsualStartAndEndTime,
            //terrain = request.terrain,
            //sitePhoto = request.sitePhoto
        )
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.putSupervisorPersonalData(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
) {
    post("putSupervisorPersonalData") {
        val request = kotlin.runCatching { call.receiveNullable<SupervisorProfile>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        SupervisorDataSource.putSupervisorPersonalData(
            email = request.email,
            firstname = request.firstName,
            lastname = request.lastName,
            personalPhoto = request.personalPhoto
        )
        call.respond(HttpStatusCode.OK)
    }
}


//aws visualiser route functions for Supervisors
// getSupervisorSignupInfo
// getSupervisorSiteInfo
// getSupervisorPersonalData
fun Route.getSupervisorSignupInfo(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    post("getSupervisorSignupInfo") {
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val profile = SupervisorDataSource.getSupervisorSignupAuth(request.email)

        val passwordBool = profile.data?.password
        val saltBool = profile.data?.salt
        val emailBool = profile.data?.email

        val password: String
        val salt: String
        val email: String

        if (saltBool == null || passwordBool == null || emailBool == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        } else {
            password = profile.data.password
            salt = profile.data.salt
            email = profile.data.email
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = password,
                salt = salt
            )
        )
        if (!isValidPassword) {
            println("Entered hash: ${DigestUtils.sha256Hex("${salt}${request.password}")}, Hashed PW: $password")
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = email
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token,
                isSupervisor = true
            )
        )

    }
}

// getSupervisorSiteInfo
fun Route.getSupervisorSiteInfo(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
) {
    authenticate {
        get("getSupervisorSiteInfo") {
            val authEmail = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
            val response = SupervisorDataSource.getSupervisorSiteInfo(email = authEmail)

            val emailBool = response.data?.email
            val addressBool = response.data?.address
            val siteExplanationBool = response.data?.siteExplanation
            val siteAddressExplanationBool = response.data?.siteAddressExplanation
            val googleMapsLocationBool = response.data?.googleMapsLocation
            val siteDaysWorkedAndThereUsualStartAndEndTimeBool =
                response.data?.siteDaysWorkedAndThereUsualStartAndEndTime
            val terrainBool = response.data?.terrain
            val sitePhotoBool = response.data?.sitePhoto

            val email: String
            val address: String
            val siteExplanation: String
            val siteAddressExplanation: String
            val googleMapsLocation: String
            val siteDaysWorkedAndThereUsualStartAndEndTime: String
            val terrain: String
            val sitePhoto: String

            if (
                emailBool == null ||
                addressBool == null ||
                siteExplanationBool == null ||
                siteAddressExplanationBool == null ||
                googleMapsLocationBool == null ||
                siteDaysWorkedAndThereUsualStartAndEndTimeBool == null ||
                terrainBool == null ||
                sitePhotoBool == null
            ) {
                call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                return@get
            } else {
                email = response.data.email
                address = response.data.address
                siteExplanation = response.data.siteExplanation
                siteAddressExplanation = response.data.siteAddressExplanation
                googleMapsLocation = response.data.googleMapsLocation
                siteDaysWorkedAndThereUsualStartAndEndTime = response.data.siteDaysWorkedAndThereUsualStartAndEndTime
                terrain = response.data.terrain
                sitePhoto = response.data.sitePhoto
            }

            val workerSite = WorkerSite(
                email = email,
                address = address,
                siteExplanation = siteExplanation,
                siteAddressExplanation = siteAddressExplanation,
                googleMapsLocation = googleMapsLocation,
                siteDaysWorkedAndThereUsualStartAndEndTime = siteDaysWorkedAndThereUsualStartAndEndTime,
                terrain = terrain,
                sitePhoto = sitePhoto
            )

            call.respond(workerSite)
        }
    }
}

// getSupervisorPersonalData
fun Route.getSupervisorPersonalData(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
) {
    authenticate {
        get("getSupervisorPersonalData") {
            val authEmail = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
            val response = SupervisorDataSource.getSupervisorPersonalData(email = authEmail)

            val emailBool = response.data?.email
            val firstnameBool = response.data?.firstName
            val lastnameBool = response.data?.lastName
            val personalPhotoBool = response.data?.personalPhoto

            val email: String
            val firstname: String
            val lastname: String
            val personalPhoto: String

            if (
                emailBool == null ||
                firstnameBool == null ||
                lastnameBool == null ||
                personalPhotoBool == null
            ) {
                call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                return@get
            } else {
                email = response.data.email
                firstname = response.data.firstName
                lastname = response.data.lastName
                personalPhoto = response.data.personalPhoto
            }

            val personal = SupervisorProfile(
                email = email,
                firstName = firstname,
                lastName = lastname,
                personalPhoto = personalPhoto
            )
            call.respond(personal)
        }
    }
}




