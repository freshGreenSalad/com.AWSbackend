package com.example.awsServices.dynamoDb.Search.DataClasses

data class WorkerSearchQuery(
    val experience: String,
    val lowerBound:Int,
    val upperBound:Int
)
