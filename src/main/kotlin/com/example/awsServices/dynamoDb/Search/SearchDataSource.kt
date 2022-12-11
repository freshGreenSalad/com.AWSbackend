package com.example.awsServices.dynamoDb.Search

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.QueryRequest
import com.example.Data.models.workerVisualiser.WorkerProfile
import com.example.awsServices.dynamoDb.Search.DataClasses.WorkerSearchQuery
import com.example.utilitys.AWSHelperFunctions
import com.example.utilitys.WorkerDynamoObjectConverters

class SearchDataSource: SearchWorkerInterface {
    override suspend fun SearchForWorker(workerSearchQuery: WorkerSearchQuery): List<WorkerProfile> {
        val attrValues = mutableMapOf<String, AttributeValue>()

        attrValues[":partitionKey"] = AttributeValue.S("experience#${workerSearchQuery.experience}")
        attrValues[":SortKeyValOne"] = AttributeValue.N(workerSearchQuery.lowerBound.toString())
        attrValues[":SortKeyValOne"] = AttributeValue.N(workerSearchQuery.upperBound.toString())
        val request = QueryRequest {
            limit = 5
            tableName = "workerAppTable"
            keyConditionExpression = "partitionKey = :partitionKey AND SortKey BETWEEN :SortKeyValOne AND :SortKeyValTwo"
            this.expressionAttributeValues = attrValues
        }
        val listOfWorkers = mutableListOf<WorkerProfile>()
        val items = AWSHelperFunctions().AWSQuerry(request)
        if (items != null) {
            for (worker in items) {
                listOfWorkers.add(
                    WorkerDynamoObjectConverters().dynamoMapToWorkerProfile(worker)
                )
            }
        }
        return listOfWorkers
    }
}