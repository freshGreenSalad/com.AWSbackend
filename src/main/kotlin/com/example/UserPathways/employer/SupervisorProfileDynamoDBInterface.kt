package com.example.UserPathways.employer

import com.example.UserPathways.employer.supervisorVisualiser.SupervisorProfile
import com.example.UserPathways.employer.supervisorVisualiser.SupervisorSite
import com.example.utilitys.wrapperClasses.AwsResultWrapper


interface SupervisorProfileDynamoDBInterface {
    suspend fun putSupervisorSiteInfo(site: SupervisorSite)
    suspend fun putSupervisorPersonalData(supervisorProfile: SupervisorProfile)
    suspend fun getSupervisorSiteInfo(email:String): AwsResultWrapper<SupervisorSite>
    suspend fun getSupervisorPersonalData(email: String): AwsResultWrapper<SupervisorProfile>
}