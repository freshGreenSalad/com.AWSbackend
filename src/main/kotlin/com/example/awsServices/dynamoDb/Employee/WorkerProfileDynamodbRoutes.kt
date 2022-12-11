package com.example.awsServices.dynamoDb

import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.DriversLicence
import com.example.Data.models.workerVisualiser.*
import com.example.utilitys.ObjectAdapters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.workerSiteInfo(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("WorkerSiteInfo") {
        val request = call.receiveOrNull<WorkerSite>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putAWSItemValues(ObjectAdapters().WorkerSiteToItemValuesMap(request))
        call.respond(HttpStatusCode.OK)
    }
    authenticate {
        get("WorkerSiteInfo") {
            val email = call.receiveOrNull<Email>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = WorkerDataSource.getWorkerSiteInfo(email.email).data!!
            call.respond(response)
        }
    }
}

fun Route.WorkerSpecialLicence(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("WorkerSpecialLicence") {
        val request = call.receiveOrNull<SpecialLicence>() ?: kotlin.run { call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putAWSItemValues(ObjectAdapters().specialLicenceToItemValues(request))
        call.respond(HttpStatusCode.OK)
    }
    authenticate {
        get("WorkerSpecialLicence") {
            val email = call.receiveOrNull<Email>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = WorkerDataSource.getWorkerSpecialLicence(email.email).data?.toList() ?: emptyList()
            call.respond(response)
        }
    }
}

fun Route.DatesWorked(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("DatesWorked") {
        val request = call.receiveOrNull<DatesWorked>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putAWSItemValues(ObjectAdapters().DatesWorkedToItemValues(request))
        call.respond(HttpStatusCode.OK)
    }
    authenticate {
        get("DatesWorked") {
            val email = call.receiveOrNull<Email>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = WorkerDataSource.getDatesWorked(email = email.email).data!!
            call.respond(response)
        }
    }
}

fun Route.WorkerDriversLicence(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("WorkerDriversLicence") {
        val request = call.receiveOrNull<DriversLicence>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putAWSItemValues(ObjectAdapters().MapWorkerLicenceToItemValues(request))
        call.respond(HttpStatusCode.OK)
    }
    authenticate {
        get("WorkerDriversLicence") {
            val email = call.receiveOrNull<Email>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = WorkerDataSource.getWorkerDriversLicence(email.email).data!!
            call.respond(response)
        }
    }
}

fun Route.WorkerExperience(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("WorkerExperience") {
        val request = call.receiveOrNull<Experience>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putAWSItemValues(ObjectAdapters().WorkerExperienceToItemValues(request))
        call.respond(HttpStatusCode.OK)
    }
    authenticate {
        get("WorkerExperience") {
            val email = call.receiveOrNull<Email>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = WorkerDataSource.getWorkerExperience(email.email).data?.toList() ?: emptyList()
            call.respond(response)
        }
    }
}

fun Route.WorkerPersonalData(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    post("WorkerPersonalData") {
        val request = call.receiveOrNull<WorkerProfile>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        WorkerDataSource.putAWSItemValues(ObjectAdapters().workerProfileToItemValues(request))
        call.respond(HttpStatusCode.OK)
    }
    authenticate {
        get("WorkerPersonalData") {
            val email = call.receiveOrNull<Email>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            println(email)
            val response = WorkerDataSource.getWorkerPersonalData(email.email).data!!
            call.respond(response)
        }
    }
}