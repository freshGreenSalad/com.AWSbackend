package com.example.Data.models.supervisorVisualiser

import com.example.Data.models.general.Location
import kotlinx.serialization.Serializable

@Serializable
data class SupervisorSite(
    val email: String,
    val address: String,
    val location: Location
)
