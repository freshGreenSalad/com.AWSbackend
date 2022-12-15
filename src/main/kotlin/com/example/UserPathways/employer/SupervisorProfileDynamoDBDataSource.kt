package com.example.UserPathways.employer

import com.example.UserPathways.employer.supervisorVisualiser.SupervisorProfile
import com.example.UserPathways.employer.supervisorVisualiser.SupervisorSite
import com.example.utilitys.wrapperClasses.AwsResultWrapper
import com.example.utilitys.AWSHelperFunctions
import com.example.utilitys.objectsToAWSMaps
import com.example.utilitys.WorkerAWSMapsToObjects

class SupervisorProfileDynamoDBDataSource(
) : SupervisorProfileDynamoDBInterface {
    override suspend fun putSupervisorSiteInfo(
        supervisorSite: SupervisorSite
    ) {
        try {
        val itemValues = objectsToAWSMaps().SiteAddressToItemValues(supervisorSite)
        val request = AWSHelperFunctions().buildPutItemRequest(itemValues)
            AWSHelperFunctions().PutObject(request)
        } catch (e: Exception) {

        }
    }

    override suspend fun putSupervisorPersonalData(
        supervisorProfile: SupervisorProfile
    ) {
        try {
            val itemValues = objectsToAWSMaps().supervisorProfiletoItemvalues(supervisorProfile)
            val request = AWSHelperFunctions().buildPutItemRequest(itemValues)
            AWSHelperFunctions().PutObject(request)
        } catch (e: Exception) {

        }
    }

    override suspend fun getSupervisorSiteInfo(email: String): AwsResultWrapper<SupervisorSite> {
        return try {
        val keyToGet = AWSHelperFunctions().KeyToGet(email, "site")
        val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
        val workerSite = WorkerAWSMapsToObjects().dynamoResultToSupervisorSite(request)
            AwsResultWrapper.Success(workerSite)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getSupervisorPersonalData(email: String): AwsResultWrapper<SupervisorProfile> {
        return try {
        val keyToGet = AWSHelperFunctions().KeyToGet(email, "personal")
        val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
            val personal = WorkerAWSMapsToObjects().dynamoResultTosupervisorProfile(request)
            AwsResultWrapper.Success(data = personal)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }
}