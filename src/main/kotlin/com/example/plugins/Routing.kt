package com.example.plugins

import com.example.*
import com.example.awsServices.dynamoDB.PutProflieInDynamoDB
import com.example.awsServices.profile.ProfileDataSource
import com.example.awsServices.profile.dynamoDbProfileDataSource
import com.plcoding.security.hashing.HashingService
import com.plcoding.security.token.TokenConfig
import com.plcoding.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*

fun Application.configureRouting(
    ProfileDataSource: ProfileDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    routing {

        //puts the initial five profiles into the cloud
        puts3()

        //gets the initial data in the cloud with the key passed in the url
        gets3()

        //used this to upload the test images to the workerImage folder on s3
        putWorkerImage()

        //put key as new row in dynamodb
        PutProflieInDynamoDB(ProfileDataSource)
    }
}
