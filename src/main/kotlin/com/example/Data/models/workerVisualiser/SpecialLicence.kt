package com.example.Data.models.workerVisualiser

@kotlinx.serialization.Serializable
data class SpecialLicence(
    val email: String,
    val licenceType:String,
    val issueDate:String,
    val expiryDate:String,
    val licencePhoto:String
)
