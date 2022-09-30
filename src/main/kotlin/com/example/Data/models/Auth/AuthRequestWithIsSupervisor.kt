package com.example.Data.models.Auth

@kotlinx.serialization.Serializable
data class AuthRequestWithIsSupervisor(
    val email:String,
    val password: String,
    val isSupervisor: Boolean
)
