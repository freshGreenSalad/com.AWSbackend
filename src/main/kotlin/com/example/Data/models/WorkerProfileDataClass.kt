package com.example.Data.models

import kotlinx.serialization.Serializable

@Serializable
data class WorkerProfileDataClass (
    val email: String,
    val firstName:String,
    val lastName: String
        )
