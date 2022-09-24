package com.example.awsServices.dynamoDb

import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.Auth.AuthRequest
import com.example.Data.models.Auth.AuthResponse
import com.example.Data.models.workerVisualiser.*
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

//aws visualiser route functions for workers
// putWorkerSignupInfo
// putWorkerSiteInfo
// putWorkerSpecialLicence
// putDatesWorked
// putWorkerPersonalData
// putWorkerExperience
fun Route.putWorkerSignupInfo(
    WorkerDataSource: WorkerProfileDynamoDBInterface,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("putWorkerSignupInfo") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
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

        val wasAcknowledged = WorkerDataSource.putWorkerSignupInfo(
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
                token = token
            )
        )
    }
}

fun Route.putWorkerSiteInfo(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("putWorkerSiteInfo") {
        val request = call.receiveOrNull<WorkerSite>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putWorkerSiteInfo(
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

fun Route.putWorkerSpecialLicence(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("putWorkerSpecialLicence") {
        val request = call.receiveOrNull<SpecialLicence>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putWorkerSpecialLicence(
            email = request.email,
            licenceType = request.licenceType,
            issueDate = request.issueDate,
            expireyDate = request.expiryDate,
            licencePhoto = request.licencePhoto
        )
        call.respond(HttpStatusCode.OK)
    }
}


fun Route.putDatesWorked(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("putDatesWorked") {
        val request = call.receiveOrNull<DatesWorked>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putDatesWorked(
            email = request.email,
            aggregate = request.aggregate,
            jan = request.jan,
            feb = request.feb,
            march = request.march,
            april = request.april,
            may = request.may,
            june = request.june,
            july = request.july,
            august = request.august,
            september = request.september,
            october = request.october,
            november = request.november,
            december = request.december
        )
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.putWorkerPersonalData(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("putWorkerPersonalData") {
        val request = call.receiveOrNull<Personal>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putWorkerPersonalData(
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

fun Route.putWorkerExperience(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("putWorkerExperience") {
        val request = call.receiveOrNull<Experience>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putWorkerExperience(
            email = request.email,
            typeofExperience = request.typeofExperience,
            ratingAggregate = request.ratingAggregate,
            previousratingsfromSupervisors = request.previousratingsfromSupervisors
        )
        call.respond(HttpStatusCode.OK)
    }
}
//aws visualiser route functions for workers
// getWorkerSignupInfo
// getWorkerSiteInfo
// getWorkerSpecialLicence
// getDatesWorked
// getWorkerPersonalData
// getWorkerExperience

//actually a post route, but it returns a JWT
fun Route.getWorkerSignupAuth(
    WorkerDataSource: WorkerProfileDynamoDBInterface,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("getWorkerSignupAuth") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val profile = WorkerDataSource.getWorkerSignupAuth(request.email)

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
                token = token
            )
        )
    }
}

fun Route.getWorkerSiteInfo(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    get("getWorkerSiteInfo") {
        val authEmail = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
        val response = WorkerDataSource.getWorkerSiteInfo(email = authEmail)

        val emailBool = response.data?.email
        val addressBool = response.data?.address
        val siteExplanationBool = response.data?.siteExplanation
        val siteAddressExplanationBool = response.data?.siteAddressExplanation
        val googleMapsLocationBool = response.data?.googleMapsLocation
        val siteDaysWorkedAndThereUsualStartAndEndTimeBool = response.data?.siteDaysWorkedAndThereUsualStartAndEndTime
        val terrainBool = response.data?.terrain
        val sitePhotoBool = response.data?.sitePhoto

        val email: String
        val address : String
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

fun Route.getWorkerSpecialLicence(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    get("getWorkerSpecialLicence") {
        val authEmail = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
        val response = WorkerDataSource.getWorkerSpecialLicence(email = authEmail)

        val emailBool = response.data?.email
        val licenceTypeBool = response.data?.licenceType
        val issueDateBool = response.data?.issueDate
        val expiryDateBool = response.data?.expiryDate
        val licencePhotoBool = response.data?.licencePhoto

        val email: String
        val licenceType: String
        val issueDate: String
        val expiryDate: String
        val licencePhoto: String

        if (
            emailBool == null ||
            licenceTypeBool == null ||
            issueDateBool == null ||
            expiryDateBool == null ||
            licencePhotoBool == null
        ) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@get
        } else {
            email = response.data.email
            licenceType = response.data.licenceType
            issueDate = response.data.issueDate
            expiryDate = response.data.expiryDate
            licencePhoto = response.data.licencePhoto
        }

        val specialLicence = SpecialLicence(
            email = email,
            licenceType = licenceType,
            issueDate = issueDate,
            expiryDate = expiryDate,
            licencePhoto = licencePhoto,
        )
        call.respond(specialLicence)
    }
}

fun Route.getDatesWorked(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    get("getDatesWorked") {
        val authEmail = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
        val response = WorkerDataSource.getDatesWorked(email = authEmail)

        val emailBool = response.data?.email
        val aggregateBool = response.data?.aggregate
        val janBool = response.data?.jan
        val febBool = response.data?.feb
        val marchBool = response.data?.march
        val aprilBool = response.data?.april
        val mayBool = response.data?.may
        val juneBool = response.data?.june
        val julyBool = response.data?.july
        val augustBool = response.data?.august
        val septemberBool = response.data?.september
        val octoberBool = response.data?.october
        val novemberBool = response.data?.november
        val decemberBool = response.data?.december

        val email: String
        val aggregate: String
        val jan: String
        val feb: String
        val march: String
        val april: String
        val may: String
        val june: String
        val july: String
        val august: String
        val september: String
        val october: String
        val november: String
        val december: String

        if (
            emailBool == null ||
            aggregateBool == null ||
            janBool == null ||
            febBool == null ||
            marchBool == null ||
            aprilBool == null ||
            mayBool == null ||
            juneBool == null ||
            julyBool == null ||
            augustBool == null ||
            septemberBool == null ||
            octoberBool == null ||
            novemberBool == null ||
            decemberBool == null
        ) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@get
        } else {
            email = response.data.email
            aggregate = response.data.aggregate
            jan = response.data.jan
            feb = response.data.feb
            march = response.data.march
            april = response.data.april
            may = response.data.may
            june = response.data.june
            july = response.data.july
            august = response.data.august
            september = response.data.september
            october = response.data.october
            november = response.data.november
            december = response.data.december
        }

        val datesWorked = DatesWorked(
            email = email,
            aggregate = aggregate,
            jan = jan,
            feb = feb,
            march = march,
            april = april,
            may = may,
            june = june,
            july = july,
            august = august,
            september = september,
            october = october,
            november = november,
            december = december,
        )

        call.respond(datesWorked)
    }
}

fun Route.getWorkerPersonalData(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    get("getWorkerPersonalData") {
        val authEmail = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
        val response = WorkerDataSource.getWorkerPersonalData(email = authEmail)

        val emailBool = response.data?.email
        val supervisorBool = response.data?.supervisor
        val firstnameBool = response.data?.firstname
        val lastnameBool = response.data?.lastname
        val recordOfAttendanceBool = response.data?.recordOfAttendance
        val rateBool = response.data?.rate
        val personalPhotoBool = response.data?.personalPhoto

        val email: String
        val supervisor: String
        val firstname: String
        val lastname: String
        val recordOfAttendance: String
        val rate: String
        val personalPhoto: String

        if (
            emailBool == null ||
            supervisorBool == null ||
            firstnameBool == null ||
            lastnameBool == null ||
            recordOfAttendanceBool == null ||
            rateBool == null ||
            personalPhotoBool == null
        ) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@get
        } else {
            email = response.data.email
            supervisor = response.data.supervisor
            firstname = response.data.firstname
            lastname = response.data.lastname
            recordOfAttendance = response.data.recordOfAttendance
            rate = response.data.rate
            personalPhoto = response.data.personalPhoto
        }

        val personal = Personal(
            email = email,
            supervisor = supervisor,
            firstname = firstname,
            lastname = lastname,
            recordOfAttendance = recordOfAttendance,
            rate = rate,
            personalPhoto = personalPhoto
        )
        call.respond(personal)
    }
}

fun Route.getWorkerExperience(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    get("getWorkerExperience") {
        val authEmail = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
        val response = WorkerDataSource.getWorkerExperience(email = authEmail)

        val emailBool = response.data?.email
        val typeofExperienceBool = response.data?.typeofExperience
        val ratingAggregateBool = response.data?.ratingAggregate
        val previousratingsfromSupervisorsBool = response.data?.previousratingsfromSupervisors

        val email: String
        val typeofExperience: String
        val ratingAggregate: String
        val previousratingsfromSupervisors: String

        if (
            emailBool == null ||
            typeofExperienceBool == null ||
            ratingAggregateBool == null ||
            previousratingsfromSupervisorsBool == null
        ) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@get
        } else {
            email = response.data.email
            typeofExperience = response.data.typeofExperience
            ratingAggregate = response.data.ratingAggregate
            previousratingsfromSupervisors = response.data.previousratingsfromSupervisors
        }

        val experience = Experience(
            email = email,
            typeofExperience = typeofExperience,
            ratingAggregate = ratingAggregate,
            previousratingsfromSupervisors = previousratingsfromSupervisors,
        )
        call.respond(experience)
    }
}
