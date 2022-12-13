package com.example.UserPathways.employer.supervisorVisualiser

import kotlinx.serialization.Serializable

@Serializable
data class SupervisorProfile(
    val email: String,
    val firstName: String,
    val lastName: String,
    val personalPhoto: String,
)
