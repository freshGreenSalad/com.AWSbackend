package com.example.Data.models.general

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val Lat:Double,
    val Lng: Double
)