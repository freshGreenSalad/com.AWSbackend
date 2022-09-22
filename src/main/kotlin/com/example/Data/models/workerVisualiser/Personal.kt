package com.example.Data.models.workerVisualiser

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
