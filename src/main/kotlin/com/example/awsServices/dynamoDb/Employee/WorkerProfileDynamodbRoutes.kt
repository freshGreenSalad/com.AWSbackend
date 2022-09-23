package com.example.awsServices.dynamoDb

import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.Auth.AuthRequest
import com.example.Data.models.Auth.AuthResponse
import com.example.Data.models.workerVisualiser.*
import com.plcoding.security.hashing.HashingService
import com.plcoding.security.token.TokenClaim
import com.plcoding.security.token.TokenConfig
import com.plcoding.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

    //aws visualiser route functions for workers
    // putWorkerSignupInfo
    // putWorkerSiteInfo
    // putWorkerSpecialLicence
    // putDatesWorked
    // putWorkerPersonalData
    // putWorkerExperience
fun Route.putWorkerEmailPassword(
    WorkerDataSource: WorkerProfileDynamoDBInterface,
    tokenService: TokenService,
    hashingService: HashingService,
    tokenConfig: TokenConfig
) {
    post("putWorkerEmailPassword") {
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

        val wasAcknowledged = WorkerDataSource.putWorkerSignupInfo(
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


