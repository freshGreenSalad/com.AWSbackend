package com.example

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.decodeToString
import aws.smithy.kotlin.runtime.content.fromFile
import com.example.Data.models.WorkerList
import com.example.Data.models.WorkerPrimaryInfo
import com.example.plugins.putS3Object
import kotlinx.serialization.decodeFromString
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import java.io.File


fun Route.puts3(){
    put( "PutInitialDataInCloud" ){
        putS3Object(
            bucketName = "testbucketletshopeitsfree",
            workerList = WorkerList
        )
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.gets3() {
    get("getInitalDataInCloud/{file}/{key}") {
        val keyname = call.parameters["file"] + "/" + call.parameters["key"]
        val request = GetObjectRequest {
            key = keyname
            bucket = "testbucketletshopeitsfree"
        }
        val response = S3Client { region = "ap-southeast-2" }.use { s3 ->
            s3.getObject(request) { resp ->
                resp.body?.decodeToString()?:""
            }
        }
        println(response)
        call.respond(Json.decodeFromString<WorkerPrimaryInfo>(response))
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.putWorkerImage(){
    put("putWorkerImage") {
        val request = PutObjectRequest {
            bucket = "testbucketletshopeitsfree"
            key = "WorkerImages/" + "5"
            body = ByteStream.fromFile(File("Drawables/nine.PNG"))
        }
        S3Client { region = "ap-southeast-2" }.use { s3 ->
            val response = s3.putObject(request)
            println("Tag information is ${response.eTag}")
        }
        call.respond(HttpStatusCode.OK)
    }
}