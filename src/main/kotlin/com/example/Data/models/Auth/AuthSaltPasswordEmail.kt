package com.example.Data.models.Auth

@kotlinx.serialization.Serializable
data class AuthSaltPasswordEmail(
    val email: String,
    val password: String,
    val salt: String
)
