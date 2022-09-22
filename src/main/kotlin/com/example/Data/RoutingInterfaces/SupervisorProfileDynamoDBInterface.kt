package com.example.Data.models


interface SupervisorProfileDynamoDBInterface {

    //new aws route from visualiser put supervisor
    suspend fun putSupervisorSignupInfo(email:String, password:String, salt:String):Boolean

    suspend fun putSupervisorSiteInfo(email:String, address:String, siteExpliation:String, siteAddressExplination:String, googleMapsLocation:String, siteDaysWorkedAndThereUsualStartAndEndTime:String, terrain:String, sitePhoto:String )

    suspend fun putSupervisorPersonalData(email: String, supervisor:String, firstname:String, lastname:String, recordOfAttendance:String, rate:String, personalPhoto:String)

}