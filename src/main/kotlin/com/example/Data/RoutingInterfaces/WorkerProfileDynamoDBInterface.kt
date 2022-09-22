package com.example.Data.RoutingInterfaces

interface WorkerProfileDynamoDBInterface {

    //new aws route from visualiser put worker
    suspend fun putWorkerSignupInfo(email:String, password:String, salt:String):Boolean

    suspend fun putWorkerSiteInfo(email:String, address:String, siteExpliation:String, siteAddressExplination:String, googleMapsLocation:String, siteDaysWorkedAndThereUsualStartAndEndTime:String, terrain:String, sitePhoto:String )

    suspend fun putWorkerSpecialLicence(email: String, licenceType:String, issueDate:String, expireyDate:String, licencePhoto:String)

    suspend fun putDatesWorked(email: String, aggregate:String, jan:String, feb:String,march:String, april:String, may:String, june:String, july:String, august:String, september:String, october:String, november:String, december:String)

    suspend fun putWorkerPersonalData(email: String, supervisor:String, firstname:String, lastname:String, recordOfAttendance:String, rate:String, personalPhoto:String)

    suspend fun putWorkerExperience(email: String, typeofExperience:String, ratingAggregate:String, previousratingsfromSupervisors:String )


}