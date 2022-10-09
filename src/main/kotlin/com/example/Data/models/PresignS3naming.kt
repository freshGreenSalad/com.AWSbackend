package com.example.Data.models

@kotlinx.serialization.Serializable
data class PresignS3naming(
    val jwt: String,
    val foldername:String,
)

