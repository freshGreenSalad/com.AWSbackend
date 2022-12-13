package com.example.UserPathways.Search

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.BatchGetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.KeysAndAttributes
import aws.sdk.kotlin.services.dynamodb.model.QueryRequest
import com.example.UserPathways.Employee.workerVisualiser.WorkerProfile
import com.example.UserPathways.Search.DataClasses.WorkerSearchQuery
import com.example.utilitys.AWSHelperFunctions

class SearchDataSource: SearchWorkerInterface {
    override suspend fun SearchForWorker(workerSearchQuery: WorkerSearchQuery): List<WorkerProfile> {
        val attrValues = mutableMapOf<String, AttributeValue>()

        attrValues[":partitionKey"] = AttributeValue.S("experience#${workerSearchQuery.experience}")
        attrValues[":SortKeyValOne"] = AttributeValue.S(workerSearchQuery.lowerBound.toString())
        attrValues[":SortKeyValTwo"] = AttributeValue.S(workerSearchQuery.upperBound.toString())
        val request = QueryRequest {
            limit = 5
            tableName = "workerAppTable"
            keyConditionExpression = "partitionKey = :partitionKey AND SortKey BETWEEN :SortKeyValOne AND :SortKeyValTwo"
            this.expressionAttributeValues = attrValues
        }
        val items = AWSHelperFunctions().AWSQuerry(request)
        val keyList:List<Map<String, AttributeValue>> = emptyList()

        if (items != null) {
            for (emailReturnedfromExperienceSearch in items) {
                val individualKey:Map<String, AttributeValue> = AWSHelperFunctions().KeyToGet((emailReturnedfromExperienceSearch["email"]!!.asS()),"personal#worker")
                keyList.plus(individualKey)
            }
        }
        val keyInitialisation = KeysAndAttributes{
            keys = keyList
        }
        val mapOfKeysToTableName = mapOf("workerAppTable" to keyInitialisation)
        val batchRequest = BatchGetItemRequest{
            requestItems = mapOfKeysToTableName
        }
        val workersInAWSForm = DynamoDbClient { region = "ap-southeast-2" }.use { db ->
            db.batchGetItem(batchRequest)
        }.responses

        val listOfWorkers = SearchObjectConverters().getBatchItemRequestToWorkerList(workersInAWSForm)
        return listOfWorkers
    }
}