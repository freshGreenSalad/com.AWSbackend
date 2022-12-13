package com.example.UserPathways.employer.supervisorVisualiser

import kotlinx.serialization.Serializable

@Serializable
data class SupervisorSite(
    val email: String,
    val address: String,
    val location: Location
)
