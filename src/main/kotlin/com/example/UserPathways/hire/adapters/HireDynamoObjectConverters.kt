package com.example.UserPathways.hire.adapters

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import com.example.utilitys.AWSHelperFunctions

class HireDynamoObjectConverters {
    suspend fun dynamoResultToListOfWorkersInHiredList(request: GetItemRequest): List<AttributeValue>? {
        val result = AWSHelperFunctions().GetItem(request)
        return if(result==null) null else {
            result["HiredWorkerMap"]?.asL()
        }
    }
}