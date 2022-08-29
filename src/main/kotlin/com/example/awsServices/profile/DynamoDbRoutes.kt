package com.example.awsServices.dynamoDB


import com.example.awsServices.profile.Profile
import com.example.awsServices.profile.ProfileDataSource
import com.example.awsServices.profile.dynamoDbProfileDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



fun Route.PutProflieInDynamoDB(
    profileDataSource: ProfileDataSource
){
    post("PutprofileInDynamodb") {
        val profile = call.receive<Profile>()
        profileDataSource.putKeyValueInTable(profile)
        call.respond(HttpStatusCode.OK)
    }
}


