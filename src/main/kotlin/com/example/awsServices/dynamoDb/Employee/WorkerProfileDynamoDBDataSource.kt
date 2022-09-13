package com.example.awsServices.dynamoDb.Employee

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.Licence
import com.example.Data.models.Profile
import com.example.Data.models.ProfileInformation
import com.example.Data.models.WorkerProfileDataClass
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WorkerProfileDynamoDBDataSource():WorkerProfileDynamoDBInterface{

        override suspend fun getProfileByEmail(email: String): Profile {
            val keyToGet = mutableMapOf<String, AttributeValue>()
            keyToGet["Email"] = AttributeValue.S(email)

            val request = GetItemRequest {
                key = keyToGet
                tableName = "WorkerProfileTable"
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
            tableName = "WorkerProfileTable"
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

    override suspend fun updateTableItem(
        email: String,
        request: ProfileInformation,
    ) {

        val itemKey = mutableMapOf<String, AttributeValue>()
        itemKey["Email"] = AttributeValue.S(email)

        val updatedValues = mutableMapOf<String, AttributeValueUpdate>()
        updatedValues["FirstName"] = AttributeValueUpdate {
            value = AttributeValue.S(Json.encodeToString(request.firstname))
            action = AttributeAction.Put
        }
        updatedValues["LastName"] = AttributeValueUpdate {
            value = AttributeValue.S(Json.encodeToString(request.lastname))
            action = AttributeAction.Put
        }
        updatedValues["licence"] = AttributeValueUpdate {
            value = AttributeValue.S(Json.encodeToString(request.driversLicence))
            action = AttributeAction.Put
        }
        updatedValues["licences"] = AttributeValueUpdate {
            value = AttributeValue.S(Json.encodeToString(request.licences))
            action = AttributeAction.Put
        }
        updatedValues["experience"] = AttributeValueUpdate {
            value = AttributeValue.S(Json.encodeToString(request.experience))
            action = AttributeAction.Put
        }
        updatedValues["supervisor"] = AttributeValueUpdate {
            value = AttributeValue.S(Json.encodeToString(request.supervisor))
            action = AttributeAction.Put
        }

        val request = UpdateItemRequest {
            tableName = "WorkerProfileTable"
            key = itemKey
            attributeUpdates = updatedValues
        }

        DynamoDbClient { region = "ap-southeast-2" }.use { ddb ->
            ddb.updateItem(request)
            println("Item in UserProfileTable was updated")
        }
    }
}