package com.example.UserPathways.Employee

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.example.UserPathways.LoginSignup.Auth.EmailPasswordIsSupervisorPushId
import com.example.UserPathways.LoginSignup.Auth.SaltPasswordEmailIsSupervisor
import com.example.Data.models.DriversLicence
import com.example.UserPathways.Employee.workerVisualiser.*
import com.example.utilitys.wrapperClasses.AwsResultWrapper
import com.plcoding.security.hashing.SaltedHash

interface WorkerProfileDynamoDBInterface {
    suspend fun putWorkerSignupInfo(WorkerSignupInfo: EmailPasswordIsSupervisorPushId, saltedHash: SaltedHash):Boolean
    suspend fun putAWSItemValues(itemValues:  MutableMap<String, AttributeValue>)

    //new aws route from visualiser get worker
    suspend fun getWorkerSignupAuth(email:String): AwsResultWrapper<SaltPasswordEmailIsSupervisor>
    suspend fun getWorkerDriversLicence(email: String): AwsResultWrapper<DriversLicence>
    suspend fun getWorkerSiteInfo(email:String): AwsResultWrapper<WorkerSite>
    suspend fun getWorkerSpecialLicence(email: String): AwsResultWrapper<MutableList<SpecialLicence>>
    suspend fun getDatesWorked(email: String): AwsResultWrapper<DatesWorked>
    suspend fun getWorkerPersonalData(email: String): AwsResultWrapper<WorkerProfile>
    suspend fun getWorkerExperience(email: String): AwsResultWrapper<MutableList<Experience>>
    suspend fun getWorkers():List<WorkerProfile>
    //delete functions
    suspend fun deleteAccount(email:String)
}