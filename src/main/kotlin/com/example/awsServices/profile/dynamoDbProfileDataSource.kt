package com.example.awsServices.profile

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest

class dynamoDbProfileDataSource(
    db: DynamoDbClient
) : ProfileDataSource {
    private val DB = db

    override suspend fun getProfileByEmail(email: String): Profile {
        TODO("Not yet implemented")
    }

    override suspend fun putKeyValueInTable(profile: Profile) {

        val tableNameVal = profile.tableName
        val key = profile.key
        val keyVal = profile.keyVal
        val firstName = profile.firstName
        val firstNameVal = profile.firstNameVal
        val lastName = profile.lastName
        val lastNameVal = profile.lastNameVal
        val email = profile.email
        val emailVal = profile.emailVal
        val company = profile.company
        val companyVal = profile.companyVal

        val itemValues = mutableMapOf<String, AttributeValue>()

        itemValues[key] = AttributeValue.S(keyVal)
        itemValues[firstName] = AttributeValue.S(firstNameVal)
        itemValues[lastName] = AttributeValue.S(lastNameVal)
        itemValues[email] = AttributeValue.S(emailVal)
        itemValues[company] = AttributeValue.S(companyVal)

        val request = PutItemRequest {
            tableName = tableNameVal
            item = itemValues
        }

        DB.use { ddb ->
            ddb.putItem(request)
            println(" A new item was placed into $tableNameVal.")
        }
    }
}