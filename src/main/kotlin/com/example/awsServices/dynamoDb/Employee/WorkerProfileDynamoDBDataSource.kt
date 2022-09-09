package com.example.awsServices.dynamoDb.Employee

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.Profile
import com.example.Data.models.WorkerProfileDataClass

class WorkerProfileDynamoDBDataSource():WorkerProfileDynamoDBInterface{

        override suspend fun getProfileByEmail(email: String): Profile {
            val keyToGet = mutableMapOf<String, AttributeValue>()
            keyToGet["Email"] = AttributeValue.S(email)

            val request = GetItemRequest {
                key = keyToGet
                tableName = "UserProfileTable"
            }

            val profile = DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                val returnedItem = db.getItem(request).item?.map { it.value.asS() }
                Profile(
                    email = returnedItem?.get(5) ?: "",
                    password = returnedItem?.get(1) ?: "",
                    salt = returnedItem?.get(0) ?: "",
                    firstName = returnedItem?.get(3) ?: "",
                    lastName = returnedItem?.get(2) ?: "",
                    company = returnedItem?.get(4) ?: "",
                )
            }
            return profile
        }

    override suspend fun putWorkerProfileInDB(WorkerProfile:WorkerProfileDataClass):Boolean{
        val itemValues = mutableMapOf<String, AttributeValue>()

        itemValues["Email"] = AttributeValue.S(WorkerProfile.email)
        itemValues["FirstName"] = AttributeValue.S(WorkerProfile.firstName)
        itemValues["LastName"] = AttributeValue.S(WorkerProfile.lastName)

        val request = PutItemRequest {
            tableName = "UserProfileTable"
            item = itemValues
        }

        return try {
            DynamoDbClient{ region = "ap-southeast-2"}.use { db ->
                db.putItem(request)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}