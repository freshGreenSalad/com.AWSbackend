package com.example.awsServices.dynamoDb.Employer

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import com.example.Data.models.Profile
import com.example.Data.models.ProfileDataSourceInterface

class EmployerProfileDynamoDBDataSource(
) : ProfileDataSourceInterface {
    override suspend fun getProfileByEmail(email: String):Profile {
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

    override suspend fun insertUser(user: Profile):Boolean {

        val itemValues = mutableMapOf<String, AttributeValue>()

        itemValues["Email"] = AttributeValue.S(user.email)
        itemValues["Password"] = AttributeValue.S(user.password)
        itemValues["Salt"] = AttributeValue.S(user.salt)
        itemValues["FirstName"] = AttributeValue.S(user.firstName)
        itemValues["LastName"] = AttributeValue.S(user.lastName)
        itemValues["Company"] = AttributeValue.S(user.company)

        val request = PutItemRequest {
            tableName = "WorkerProfileTable"
            item = itemValues
        }
        var successfulInsert: Boolean
        DynamoDbClient { region = "ap-southeast-2" }.use { ddb ->
            ddb.putItem(request)
            println(" A new item was placed into WorkerProfileTable.")
            successfulInsert = true
        }
        return successfulInsert
    }
}