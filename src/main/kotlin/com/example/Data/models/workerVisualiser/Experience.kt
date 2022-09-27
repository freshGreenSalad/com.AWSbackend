package com.example.Data.models.workerVisualiser

@kotlinx.serialization.Serializable
data class Experience(
    val email: String,
    val typeofExperience: String,
    val ratingAggregate: String,
    val previousRatingsFromSupervisors: String
)

