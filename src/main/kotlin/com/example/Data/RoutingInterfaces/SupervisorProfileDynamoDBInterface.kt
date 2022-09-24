package com.example.Data.models

import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.example.Data.models.workerVisualiser.*
import com.example.Data.wrapperClasses.AwsResultWrapper


interface SupervisorProfileDynamoDBInterface {

    //new aws route from visualiser put supervisor
    suspend fun putSupervisorSignupInfo(email:String, password:String, salt:String):Boolean

    suspend fun putSupervisorSiteInfo(email:String, address:String, siteExpliation:String, siteAddressExplination:String, googleMapsLocation:String, siteDaysWorkedAndThereUsualStartAndEndTime:String, terrain:String, sitePhoto:String )

    suspend fun putSupervisorPersonalData(email: String, supervisor:String, firstname:String, lastname:String, recordOfAttendance:String, rate:String, personalPhoto:String)

    //new aws route from visualiser get supervisor
    suspend fun getSupervisorSignupAuth(email:String): AwsResultWrapper<AuthSaltPasswordEmail>

    suspend fun getSupervisorSiteInfo(email:String): AwsResultWrapper<WorkerSite>

    suspend fun getSupervisorPersonalData(email: String): AwsResultWrapper<Personal>

}