package com.example

import com.example.plugins.PresignPutRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.presignS3StorageRequest() {
    authenticate {
        put("s3PresignPut") {
            val email = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
            val fileNaming = call.receiveOrNull<String>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val uri = PresignPutRequest(
                email = email,
                fileNaming = fileNaming
            )
            call.respond(HttpStatusCode.OK, uri.toString())
        }
    }
}