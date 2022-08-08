package com.example

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.ListObjectsV2Request
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.s3B(
) {
    get("s3") {
        val s3 = S3Client.fromEnvironment {
            region = "ap-southeast-2"
        }

        val req = ListObjectsV2Request {
            bucket = "testbucketletshopeitsfree"
        }

        val response = s3.listObjectsV2(req).contents?.map { it.key }



      //  response.contents?.forEach { println(it.key) }
        call.respondText(response.toString())
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.helloWorld(){
    get("/") {
        call.respondText("Hello World!")
    }
}