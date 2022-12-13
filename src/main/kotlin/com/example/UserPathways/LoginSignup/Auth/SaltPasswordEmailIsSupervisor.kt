package com.example.UserPathways.LoginSignup.Auth

@kotlinx.serialization.Serializable
data class SaltPasswordEmailIsSupervisor(
    val email: String,
    val password: String,
    val salt: String,
    val isSupervisor: Boolean
)
