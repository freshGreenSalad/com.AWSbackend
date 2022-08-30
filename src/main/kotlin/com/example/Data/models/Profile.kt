package com.example.Data.models

@kotlinx.serialization.Serializable
data class Profile(
    val email :String,
    val password:String,
    val salt:String,
    val firstName :String,
    val lastName :String,
    val company :String,
)