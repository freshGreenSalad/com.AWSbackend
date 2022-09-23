package com.example.Data.models.Auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)