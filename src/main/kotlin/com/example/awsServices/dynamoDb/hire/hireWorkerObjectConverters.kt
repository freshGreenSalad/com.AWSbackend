package com.example.awsServices.dynamoDb.hire

import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import com.example.utilitys.AWSHelperFunctions

class hireWorkerObjectConverters {
    suspend fun dynamoResultToListOfEmails(request: GetItemRequest): List<String> {
        val result = AWSHelperFunctions().GetItem(request)!!
        val listofAttributeValues = result["HiredWorkerMap"]?.asL()
        if (listofAttributeValues == null) {
            return emptyList()
        } else {
            val listOfWorkerEmails = mutableListOf<String>()
            for (WorkerEmailInAttributeValue in listofAttributeValues) {
                val workerEmail = WorkerEmailInAttributeValue.asS()
                listOfWorkerEmails.add(workerEmail)
            }
            return listOfWorkerEmails
        }
    }
}