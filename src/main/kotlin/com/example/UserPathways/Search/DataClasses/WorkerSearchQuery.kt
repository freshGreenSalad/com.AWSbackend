package com.example.UserPathways.Search.DataClasses

import kotlinx.serialization.Serializable

@Serializable
data class WorkerSearchQuery(
    val experience: String,
    val lowerBound:Int,
    val upperBound:Int
)
