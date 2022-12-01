package com.example.Data.models

import com.example.Data.models.Auth.AuthRequestWithIsSupervisor
import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.example.Data.models.general.Location
import com.example.Data.models.supervisorVisualiser.SupervisorProfile
import com.example.Data.models.supervisorVisualiser.SupervisorSite
import com.example.Data.models.workerVisualiser.*
import com.example.Data.wrapperClasses.AwsResultWrapper
import com.plcoding.security.hashing.SaltedHash


interface SupervisorProfileDynamoDBInterface {
    suspend fun putSupervisorSiteInfo(site: SupervisorSite)
    suspend fun putSupervisorPersonalData(supervisorProfile: SupervisorProfile)
    suspend fun getSupervisorSiteInfo(email:String): AwsResultWrapper<WorkerSite>
    suspend fun getSupervisorPersonalData(email: String): AwsResultWrapper<SupervisorProfile>
}