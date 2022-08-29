package com.example.awsServices.profile

@kotlinx.serialization.Serializable
data class Profile(
    val tableName:String,
    val key:String,
    val keyVal:String,
    val firstName :String,
    val firstNameVal:String,
    val lastName :String,
    val lastNameVal :String,
    val email :String,
    val emailVal :String,
    val company :String,
    val companyVal :String
)