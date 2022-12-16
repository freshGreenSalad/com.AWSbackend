package com.example.UserPathways.hire

import com.example.UserPathways.Employee.workerVisualiser.Email
import com.example.UserPathways.hire.interfaces.HireWorkerDataSourceInterface
import com.example.awsServices.UserPathways.hire.HireWorker
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.HireWorker(
    dataSourceInterface: HireWorkerDataSourceInterface
) {
    post("hireWorker") {
        val request = kotlin.runCatching { call.receiveNullable<HireWorker>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        dataSourceInterface.pushNotificationToHiredWorker(request)
        dataSourceInterface.addWorkerToSupervisorTeam(request)
        call.respond(HttpStatusCode.OK)
    }

    get ("hireWorker"){
        val email = call.request.headers["email"]!!
        val response = dataSourceInterface.getListOfSavedWorkers(email).data?:"failed to get list"
        call.respond(response)
    }
}