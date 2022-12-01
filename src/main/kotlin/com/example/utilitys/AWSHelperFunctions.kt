package com.example.utilitys

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import aws.sdk.kotlin.services.dynamodb.model.QueryRequest
import com.example.Data.models.workerVisualiser.SpecialLicence

class AWSHelperFunctions {

    fun buildPutItemRequest(itemValues: MutableMap<String, AttributeValue>) =
        PutItemRequest {
            tableName = "workerAppTable"
            item = itemValues
        }

    fun BuildGetItemRequest(keyToGet: MutableMap<String, AttributeValue>) =
        GetItemRequest {
            key = keyToGet
            tableName = "workerAppTable"
        }
    //

    suspend fun PutObject(request: PutItemRequest) {
        DynamoDbClient { region = "ap-southeast-2" }.use { db ->
            db.putItem(request)
        }
    }

    suspend fun AWSQuerry(request: QueryRequest) =
        DynamoDbClient { region = "ap-southeast-2" }.use { db ->
            db.query(request)
        }.items

    suspend fun GetItem(request: GetItemRequest):Map<String, AttributeValue>? {
        return DynamoDbClient { region = "ap-southeast-2" }.use { db ->
            db.getItem(request)
        }.item
    }
    //

    fun KeyToGet(email: String, SortKeyValue:String): MutableMap<String, AttributeValue> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["SortKey"] = AttributeValue.S(SortKeyValue)
        return keyToGet
    }

    fun returnQuerryRequest(attributeToFind:String): QueryRequest {
        val attrValues = mutableMapOf<String, AttributeValue>()
        attrValues[":partitionKey"] = AttributeValue.S("email")
        attrValues[":SortKey"] = AttributeValue.S(attributeToFind)
        return QueryRequest {
            tableName = "workerAppTable"
            keyConditionExpression = "partitionKey = :partitionKey AND begins_with(SortKey, :SortKey)"
            this.expressionAttributeValues = attrValues
        }
    }
    suspend fun tryCatchPutObjectInDynamoDb(request: PutItemRequest) {
        try {
            PutObject(request)
        } catch (e: Exception) {
        }
    }

    fun addQueeryResultsintoSingleListOfObjects(result: List<Map<String, AttributeValue>>?): MutableList<SpecialLicence> {
        val listOfLicence = mutableListOf<SpecialLicence>()
        if (result != null) {
            for (item in result) {
                val specialLicence = WorkerDynamoObjectConverters().dynamoResultToSpecialLicence(item)
                listOfLicence.add(specialLicence)
            }
        }
        return listOfLicence
    }
}