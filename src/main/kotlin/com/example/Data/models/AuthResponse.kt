package com.example.Data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)