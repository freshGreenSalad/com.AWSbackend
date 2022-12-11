package com.example.awsServices.dynamoDb.Search

import com.example.awsServices.dynamoDb.Search.DataClasses.WorkerSearchQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.SearchWorkers(
    SearchWorkers: SearchWorkerInterface
) {
    authenticate {
        post("deleteAccount") {
            val workerSearchQuerry = kotlin.runCatching { call.receiveNullable<WorkerSearchQuery>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
            SearchWorkers.SearchForWorker(workerSearchQuerry)
            call.respond(HttpStatusCode.OK)
        }
    }
}