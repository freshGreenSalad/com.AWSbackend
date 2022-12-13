package com.example.UserPathways.LoginSignup.Auth

import kotlinx.serialization.Serializable

@Serializable
data class TokinIsSupervisor(
    val token: String,
    val isSupervisor: Boolean
)