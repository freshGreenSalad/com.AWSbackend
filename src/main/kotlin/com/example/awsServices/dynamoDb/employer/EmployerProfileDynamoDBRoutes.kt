package com.example.awsServices.dynamoDb.employer

import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.SupervisorProfileDynamoDBInterface
import com.example.Data.models.supervisorVisualiser.SupervisorProfile
import com.example.Data.models.supervisorVisualiser.SupervisorSite
import com.example.Data.models.workerVisualiser.Email
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.SupervisorSiteInfo(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
) {
    post("SupervisorSiteInfo") {
        val request = kotlin.runCatching { call.receiveNullable<SupervisorSite>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        SupervisorDataSource.putSupervisorSiteInfo(request)
        call.respond(HttpStatusCode.OK)
    }

    authenticate {
        get("SupervisorSiteInfo") {
            val email = call.receiveOrNull<Email>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = SupervisorDataSource.getSupervisorSiteInfo(email.email).data!!
            call.respond(response)
        }
    }
}

fun Route.SupervisorPersonalData(
    SupervisorDataSource: SupervisorProfileDynamoDBInterface,
) {
    post("SupervisorPersonalData") {
        val request = kotlin.runCatching { call.receiveNullable<SupervisorProfile>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        SupervisorDataSource.putSupervisorPersonalData(request)
        call.respond(HttpStatusCode.OK)
    }
    authenticate {
        get("SupervisorPersonalData") {
            val email = call.receiveOrNull<Email>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = SupervisorDataSource.getSupervisorPersonalData(email.email).data!!
            val personal = SupervisorProfile(
                email = response.email,
                firstName = response.firstName,
                lastName = response.lastName,
                personalPhoto = response.personalPhoto
            )
            call.respond(personal)
        }
    }

}

fun Route.getWorkerS(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    get("getListOfWorkerAccounts") {
        val response = WorkerDataSource.getWorkers()
        call.respond(response)
    }
}


