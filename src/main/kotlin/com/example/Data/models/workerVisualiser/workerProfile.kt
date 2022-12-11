package com.example.Data.models.workerVisualiser

import kotlinx.serialization.Serializable

@Serializable
data class WorkerProfile(
    val email: String,
    val firstName: String,
    val lastName: String,
    val personalPhoto: String,
    val rate: Int
)

val failedWorkerProfile = WorkerProfile(
    email = "failEmail",
    firstName = "failLastname",
    lastName = "asdf",
    personalPhoto = "as",
    rate = 45,
)