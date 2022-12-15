package com.example.UserPathways.Search

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.example.UserPathways.Employee.workerVisualiser.WorkerProfile
import com.example.utilitys.Constants

class SearchObjectConverters {
    fun getBatchItemRequestToWorkerList( confusingshit:Map<String, List<Map<String, AttributeValue>>>?):List<WorkerProfile>{
        val table  = confusingshit?.get(Constants.tableName)!!
        val listOfWorkers:List<WorkerProfile> = emptyList()
        for (workerInAWS in table){
            val worker = workerConverter(workerInAWS)
            listOfWorkers.plus(worker)
        }
        return listOfWorkers
    }
    fun workerConverter(wokerinAWS: Map<String, AttributeValue>):WorkerProfile{
        return WorkerProfile(
            email = wokerinAWS["partitionKey"]?.asS() ?: "",
            firstName = wokerinAWS["firstname"]?.asS() ?: "",
            lastName = wokerinAWS["lastname"]?.asS() ?: "",
            personalPhoto = wokerinAWS["personalPhoto"]?.asS() ?: "",
            rate = wokerinAWS["rate"]?.asN()?.toInt() ?: 0,
        )
    }
}