package com.example.Data.models.general

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val Lat: Double,
    val Lng: Double
)