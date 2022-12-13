package com.example.UserPathways.Search

import com.example.UserPathways.Search.DataClasses.WorkerSearchQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.SearchWorkers(
    SearchWorkers: SearchWorkerInterface
) {
    authenticate {
        get("SearchForWorkers") {
            val jsonraw = call.request.headers["Json"]!!
            val workerSearchQuery = Json.decodeFromString<WorkerSearchQuery>(jsonraw)
            println(workerSearchQuery)
            val workers = SearchWorkers.SearchForWorker(workerSearchQuery)
            call.respond(HttpStatusCode.OK, workers)
        }
    }
}