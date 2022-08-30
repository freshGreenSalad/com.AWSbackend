package com.example.plugins

import com.example.*
import com.example.Data.models.ProfileDataSourceInterface
import com.example.awsServices.dynamoDb.*
import com.plcoding.security.hashing.HashingService
import com.plcoding.security.token.TokenConfig
import com.plcoding.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    ProfileDataSource: ProfileDataSourceInterface,
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

        //puts a Profile into the dynamodb database
        PutProflieInDynamoDB(ProfileDataSource,hashingService)

        //checks a password email combination and returns a JWT
        authoriseUser(ProfileDataSource,hashingService,tokenService,tokenConfig)

        testGetProfileFromDynamodb(ProfileDataSource)

        //tests the authentication of the JWT
        authenticate()
    }
}
