package com.example.awsServices.dynamoDb

import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.WorkerProfileDataClass
import io.ktor.server.routing.*

fun Route.PopulateWorkerDBWithProfiles(
    WorkerdataSource: WorkerProfileDynamoDBInterface
) {
    val listOfFirstNames =
        listOf("Liam", "David", "Lachlan", "Brodie", "Sarah", "Ricky", "Bobby", "Geoff", "Amy", "Conor")
    val listOfLastNames =
        listOf("Daniels", "Jacobs", "Small", "Taylor", "Smith", "Everlans", "Ryan", "Mufasa", "Fox", "Cooper")
    val emailString = "email@email.com"
    post("PopulateWorkerDBwithProfile") {
        for (profile in 1..100) {
            val email = profile.toString() + emailString
            val firstname = listOfFirstNames[kotlin.random.Random.nextInt(0, 10)]
            val lastName = listOfLastNames[kotlin.random.Random.nextInt(0, 10)]
            val workerProfile = WorkerProfileDataClass(
                email = email,
                firstName = firstname,
                lastName = lastName
            )
            WorkerdataSource.putWorkerProfileInDB(workerProfile)
        }
    }
}