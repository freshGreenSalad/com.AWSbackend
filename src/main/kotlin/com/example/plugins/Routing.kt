package com.example.plugins

import com.example.helloWorld
import com.example.s3B
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {

    routing {
        s3B()
        helloWorld()
    }
}
