package com.example.UserPathways.LoginSignup.Auth

@kotlinx.serialization.Serializable
data class EmailPasswordIsSupervisor(
    val email:String,
    val password: String,
    val isSupervisor: Boolean
)
