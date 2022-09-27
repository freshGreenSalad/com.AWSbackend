package com.example.Data.RoutingInterfaces

import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.example.Data.models.workerVisualiser.*
import com.example.Data.wrapperClasses.AwsResultWrapper

interface WorkerProfileDynamoDBInterface {

    //new aws route from visualiser put worker
    suspend fun putWorkerSignupInfo(email:String, password:String, salt:String):Boolean

    suspend fun putWorkerSiteInfo(email:String, address:String, siteExplanation:String, siteAddressExplanation:String, googleMapsLocation:String, siteDaysWorkedAndThereUsualStartAndEndTime:String, terrain:String, sitePhoto:String )

    suspend fun putWorkerSpecialLicence(email: String, licenceType:String, issueDate:String, expiryDate:String, licencePhoto:String)

    suspend fun putDatesWorked(email: String, aggregate:String, jan:String, feb:String,march:String, april:String, may:String, june:String, july:String, august:String, september:String, october:String, november:String, december:String)

    suspend fun putWorkerPersonalData(email: String, supervisor:String, firstname:String, lastname:String, recordOfAttendance:String, rate:String, personalPhoto:String)

    suspend fun putWorkerExperience(email: String, typeofExperience:String, ratingAggregate:String, previousRatingsFromSupervisors:String )

    //new aws route from visualiser get worker
    suspend fun getWorkerSignupAuth(email:String): AwsResultWrapper<AuthSaltPasswordEmail>

    suspend fun getWorkerSiteInfo(email:String): AwsResultWrapper<WorkerSite>

    suspend fun getWorkerSpecialLicence(email: String): AwsResultWrapper<MutableList<SpecialLicence>>

    suspend fun getDatesWorked(email: String): AwsResultWrapper<DatesWorked>

    suspend fun getWorkerPersonalData(email: String): AwsResultWrapper<Personal>

    suspend fun getWorkerExperience(email: String): AwsResultWrapper<MutableList<Experience>>


}