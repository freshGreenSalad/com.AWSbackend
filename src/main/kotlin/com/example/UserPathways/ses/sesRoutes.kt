package com.example.UserPathways.ses

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.testSendEmail(
){
    post("testEmail") {
        testSendEmail(
            sender = "tamakilabour.small@gmail.com",
            recipient = "csma174@aucklanduni.ac.nz",
            subjectVal = "test"
        )
        call.respond(HttpStatusCode.OK)
    }
}