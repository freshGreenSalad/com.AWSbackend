package com.example.Data.models.workerVisualiser

import com.example.Data.models.Licence
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class Personal(
    val email: String,
    val supervisor:String,
    val firstname:String,
    val lastname:String,
    val recordOfAttendance:String,
    val rate:String,
    val personalPhoto:String
)


@Serializable
data class WorkerProfile(
    val email: String,
    val firstName: String,
    val lastName: String,
    val licence: Licence,
    val personalPhoto: String,
    val rate: Int
)
