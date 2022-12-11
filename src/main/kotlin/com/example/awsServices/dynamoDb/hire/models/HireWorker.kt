package com.example.awsServices.dynamoDb.hire

import kotlinx.serialization.Serializable

@Serializable
data class HireWorker(
    val supervisorEmail: String,
    val WorkerEmail:String,
    val Date:WorkerDate
)

@Serializable
data class WorkerDate(
    val day:Int,
    val month:Int,
    val year:Int
)
