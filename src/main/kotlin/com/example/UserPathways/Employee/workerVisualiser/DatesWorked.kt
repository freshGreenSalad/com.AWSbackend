package com.example.UserPathways.Employee.workerVisualiser

@kotlinx.serialization.Serializable
data class DatesWorked(
    val email: String,
    val recordOfAttendance: String,
    val jan: String,
    val feb: String,
    val march: String,
    val april: String,
    val may: String,
    val june: String,
    val july: String,
    val august: String,
    val september: String,
    val october: String,
    val november: String,
    val december: String
)
