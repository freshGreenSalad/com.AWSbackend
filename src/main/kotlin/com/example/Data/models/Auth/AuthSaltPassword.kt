package com.example.Data.models.Auth

@kotlinx.serialization.Serializable
data class AuthSaltPassword(
    val password: String,
    val salt: String
)
