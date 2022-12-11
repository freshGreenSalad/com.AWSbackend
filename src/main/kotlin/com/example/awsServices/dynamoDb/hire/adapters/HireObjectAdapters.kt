package com.example.awsServices.dynamoDb.hire.adapters

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.example.awsServices.dynamoDb.hire.HireWorker

class HireObjectAdapters {
    fun HireWorkerToItemValues(hireWorker: HireWorker, hiredworkersList:List<AttributeValue>): MutableMap<String, AttributeValue>{
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(hireWorker.supervisorEmail)
        itemValues["SortKey"] = AttributeValue.S("HiredWorkers")
        itemValues["HiredWorkerMap"] = AttributeValue.L(hiredworkersList)
        return itemValues
    }
}