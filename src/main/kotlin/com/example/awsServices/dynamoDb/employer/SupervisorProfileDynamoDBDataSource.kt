package com.example.awsServices.dynamoDb.employer

import com.example.Data.models.Auth.AuthRequestWithIsSupervisor
import com.example.Data.models.SupervisorProfileDynamoDBInterface
import com.example.Data.models.supervisorVisualiser.SupervisorProfile
import com.example.Data.models.supervisorVisualiser.SupervisorSite
import com.example.Data.models.workerVisualiser.WorkerSite
import com.example.Data.wrapperClasses.AwsResultWrapper
import com.example.utilitys.AWSHelperFunctions
import com.example.utilitys.ObjectAdapters
import com.example.utilitys.WorkerDynamoObjectConverters
import com.plcoding.security.hashing.SaltedHash

class SupervisorProfileDynamoDBDataSource(
) : SupervisorProfileDynamoDBInterface {
    override suspend fun putSupervisorSiteInfo(
        supervisorSite: SupervisorSite
    ) {
        try {
        val itemValues = ObjectAdapters().SiteAddressToItemValues(supervisorSite)
        val request = AWSHelperFunctions().buildPutItemRequest(itemValues)
            AWSHelperFunctions().PutObject(request)
        } catch (e: Exception) {

        }
    }

    override suspend fun putSupervisorPersonalData(
        supervisorProfile: SupervisorProfile
    ) {
        try {
            val itemValues = ObjectAdapters().supervisorProfiletoItemvalues(supervisorProfile)
            val request = AWSHelperFunctions().buildPutItemRequest(itemValues)
            AWSHelperFunctions().PutObject(request)
        } catch (e: Exception) {

        }
    }

    override suspend fun getSupervisorSiteInfo(email: String): AwsResultWrapper<WorkerSite> {
        return try {
        val keyToGet = AWSHelperFunctions().KeyToGet(email, "site")
        val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
        val workerSite = WorkerDynamoObjectConverters().dynamoResultToWorkerSite(request)
            AwsResultWrapper.Success(workerSite)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getSupervisorPersonalData(email: String): AwsResultWrapper<SupervisorProfile> {
        return try {
        val keyToGet = AWSHelperFunctions().KeyToGet(email, "personal")
        val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
            val personal = WorkerDynamoObjectConverters().dynamoResultTosupervisorProfile(request)
            AwsResultWrapper.Success(data = personal)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }
}