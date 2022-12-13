package com.example.UserPathways.hire

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.example.UserPathways.hire.adapters.HireDynamoObjectConverters
import com.example.utilitys.AWSHelperFunctions

class HiredWorkerList {
    suspend fun addWorkerToHiredWorkerList(supervisoremail:String, workerEmail:String):List<AttributeValue>?{
        val workerHiredList = getworkerhiredlist(supervisoremail)
        return if (workerHiredList==null){
            listOf<AttributeValue>(AttributeValue.S(workerEmail))
        }else{
            if(AttributeValue.S(workerEmail) in workerHiredList){
                return null
            }else{
                workerHiredList.plus(AttributeValue.S(workerEmail))
            }
        }
    }

    private suspend fun getworkerhiredlist(supervisorEmail:String):List<AttributeValue>?{
        val keyToGet = AWSHelperFunctions().KeyToGet(supervisorEmail, "HiredWorkers")
        val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
        val list = HireDynamoObjectConverters().dynamoResultToListOfWorkersInHiredList(request)// now this can be null if no list
        return list
    }
}


