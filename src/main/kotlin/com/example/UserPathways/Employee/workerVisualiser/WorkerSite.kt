package com.example.UserPathways.Employee.workerVisualiser

@kotlinx.serialization.Serializable
data class WorkerSite(
    val email:String,
    val address:String,
    val siteExplanation:String,
    val siteAddressExplanation:String,
    val googleMapsLocation:String,
    val siteDaysWorkedAndThereUsualStartAndEndTime:String,
    val terrain:String,
    val sitePhoto:String
)
