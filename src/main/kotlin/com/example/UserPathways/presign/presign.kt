package com.example.plugins

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.presigners.presign
import aws.smithy.kotlin.runtime.http.Url
import kotlin.time.DurationUnit
import kotlin.time.toDuration


suspend fun PresignPutRequest(email:String, fileNaming:String):Url {
    val hash = getRandomString(20)
    val request = PutObjectRequest {
        bucket = "testbucketletshopeitsfree"
        key = email+fileNaming+hash
    }
    val clientConfig = S3Client { region = "ap-southeast-2" }.config
    val presign = request.presign(
        config = clientConfig,
        duration = (60*1000*10).toDuration(DurationUnit.MILLISECONDS)
    )
    return presign.url
}

private fun getRandomString(length: Int) : String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}

