package com.example.UserPathways.LoginSignup.Auth

import kotlinx.serialization.Serializable

@Serializable
data class EmailPassword(
    val email:String,
    val password: String
)
