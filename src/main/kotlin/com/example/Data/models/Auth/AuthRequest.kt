package com.example.Data.models.Auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email:String,
    val password: String
)
