package com.example.Data.models

import com.example.Data.models.supervisorVisualiser.SupervisorProfile
import com.example.Data.models.supervisorVisualiser.SupervisorSite
import com.example.Data.wrapperClasses.AwsResultWrapper


interface SupervisorProfileDynamoDBInterface {
    suspend fun putSupervisorSiteInfo(site: SupervisorSite)
    suspend fun putSupervisorPersonalData(supervisorProfile: SupervisorProfile)
    suspend fun getSupervisorSiteInfo(email:String): AwsResultWrapper<SupervisorSite>
    suspend fun getSupervisorPersonalData(email: String): AwsResultWrapper<SupervisorProfile>
}