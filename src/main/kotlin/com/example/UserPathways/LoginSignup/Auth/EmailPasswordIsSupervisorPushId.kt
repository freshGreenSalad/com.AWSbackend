package com.example.UserPathways.LoginSignup.Auth

@kotlinx.serialization.Serializable
data class EmailPasswordIsSupervisorPushId(
    val email:String,
    val password: String,
    val isSupervisor: Boolean,
    val pushId:String
)
