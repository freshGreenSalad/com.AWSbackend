package com.example.awsServices.dynamoDb.hire

import com.example.Data.models.workerVisualiser.Email
import com.example.awsServices.dynamoDb.hire.interfaces.HireWorkerDataSourceInterface
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.HireWorker(
    dataSourceInterface: HireWorkerDataSourceInterface
) {
    post("hireWorker") {
        val request = kotlin.runCatching { call.receiveNullable<HireWorker>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        dataSourceInterface.addWorkerToSupervisorTeam(request)
        call.respond(HttpStatusCode.OK)
    }

    get ("hireWorker"){
        val email = call.receiveOrNull<Email>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val response = dataSourceInterface.getListOfSavedWorkers(email.email).data?:"failed to get list"
        call.respond(response)
    }
}