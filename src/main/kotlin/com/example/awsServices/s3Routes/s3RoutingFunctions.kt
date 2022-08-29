package com.example.plugins

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.decodeToString
import com.example.Data.models.WorkerPrimaryInfo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

suspend fun putS3Object(bucketName: String, workerList: List<WorkerPrimaryInfo>) {

    for(worker in workerList) {
        val metadataVal = mutableMapOf<String, String>()
        metadataVal[worker.name] = "test"

        val request = PutObjectRequest {
            bucket = bucketName
            key = "WorkerPrimaryInfo/" + worker.key.toString()
            metadata = metadataVal
            body =  ByteStream.fromString(Json.encodeToString(worker))

        }
        S3Client { region = "ap-southeast-2" }.use { s3 ->
            val response = s3.putObject(request)
            println("Tag information is ${response.eTag}")
        }
    }
}

suspend fun getObjectBytes(bucketName: String, keyName: String): String? {

    val request = GetObjectRequest {
        key = keyName
        bucket = bucketName
    }
    val response = S3Client { region = "ap-southeast-2" }.use { s3 ->
        s3.getObject(request) { resp ->
            resp.body?.decodeToString()
        }
    }
    return response
}

