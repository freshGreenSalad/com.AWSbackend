package com.example.UserPathways.Employee

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import com.example.UserPathways.LoginSignup.Auth.EmailPasswordIsSupervisorPushId
import com.example.UserPathways.LoginSignup.Auth.SaltPasswordEmailIsSupervisor
import com.example.Data.models.DriversLicence
import com.example.UserPathways.Employee.workerVisualiser.*
import com.example.utilitys.wrapperClasses.AwsResultWrapper
import com.example.utilitys.AWSHelperFunctions
import com.example.utilitys.Constants
import com.example.utilitys.objectsToAWSMaps
import com.example.utilitys.WorkerAWSMapsToObjects
import com.plcoding.security.hashing.SaltedHash

class WorkerProfileDynamoDBDataSource(
) : WorkerProfileDynamoDBInterface {
    override suspend fun putWorkerSignupInfo(
        WorkerSignupInfo: EmailPasswordIsSupervisorPushId,
        saltedHash: SaltedHash
    ): Boolean {
        return try {
            val itemValues = objectsToAWSMaps().SignupInfoToItemValues(WorkerSignupInfo, saltedHash)
            val request = AWSHelperFunctions().buildPutItemRequest(itemValues)
            AWSHelperFunctions().PutObject(request)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun putAWSItemValues(itemValues:  MutableMap<String, AttributeValue>) {
        val request = AWSHelperFunctions().buildPutItemRequest(itemValues)
        AWSHelperFunctions().tryCatchPutObjectInDynamoDb(request)
    }

    override suspend fun getWorkerSignupAuth(email: String): AwsResultWrapper<SaltPasswordEmailIsSupervisor> {
        return try {
            val keyToGet = AWSHelperFunctions().KeyToGet(email,"signIn")
            val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
            val authSaltPasswordEmail = WorkerAWSMapsToObjects().dynamoResultToDriverslicence(request, email)
            return AwsResultWrapper.Success(data = authSaltPasswordEmail)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getWorkerDriversLicence(email: String): AwsResultWrapper<DriversLicence> {
        return try {
            val keyToGet = AWSHelperFunctions().KeyToGet(email,"driversLicence")
            val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
            val workerSite = WorkerAWSMapsToObjects().dynamoResultToDriverslicence(request)
            AwsResultWrapper.Success(data = workerSite)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getWorkerSiteInfo(email: String): AwsResultWrapper<WorkerSite> {
        return try {
            val keyToGet = AWSHelperFunctions().KeyToGet(email,"site")
            val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
            val workerSite = WorkerAWSMapsToObjects().dynamoResultToWorkerSite(request)
            return AwsResultWrapper.Success(data = workerSite)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getDatesWorked(email: String): AwsResultWrapper<DatesWorked> {
        return try {
            val keyToGet = AWSHelperFunctions().KeyToGet(email,"datesWorked")
            val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
            val datesWorked = WorkerAWSMapsToObjects().dynamoResultToDatesWorked(request)
            return AwsResultWrapper.Success(data = datesWorked)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getWorkerPersonalData(email: String): AwsResultWrapper<WorkerProfile> {
        val keyToGet = AWSHelperFunctions().KeyToGet(email,"personal#worker")
        val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
        val item = AWSHelperFunctions().GetItem(request)!!
        return try {
            val personal = WorkerAWSMapsToObjects().dynamoMapToWorkerProfile(item)
            return AwsResultWrapper.Success(data = personal)
        } catch (e: Exception) {
            AwsResultWrapper.Success(WorkerProfile("","","","",56))
        }
    }

    override suspend fun getWorkerSpecialLicence(email: String): AwsResultWrapper<MutableList<SpecialLicence>> {
        return try {
        val request = AWSHelperFunctions().returnQuerryRequest(email = email,"licence" )
        val result = AWSHelperFunctions().AWSQuerry(request)
            val listOfLicence = AWSHelperFunctions().addQueeryResultsintoSingleListOfObjects(result)
            AwsResultWrapper.Success(listOfLicence)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getWorkerExperience(email: String): AwsResultWrapper<MutableList<Experience>> {
        return try {
            val request = AWSHelperFunctions().returnQuerryRequest(email = email, "experience")
            val listOfExperience = mutableListOf<Experience>()
            val result = AWSHelperFunctions().AWSQuerry(request)
            if (result != null) {
                for (item in result) {
                    val experience = WorkerAWSMapsToObjects().dynamoResultToExperience(item)
                    listOfExperience.add(experience)
                }
            }
            AwsResultWrapper.Success(data = listOfExperience)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getWorkers(): List<WorkerProfile> {
        val attrValues = mutableMapOf<String, AttributeValue>()
        attrValues[":SortKey"] = AttributeValue.S("personal#worker")
        val request = QueryRequest {
            limit = 5
            tableName = Constants.tableName
            indexName = "reverseLookup"
            keyConditionExpression = "SortKey = :SortKey"
            this.expressionAttributeValues = attrValues
        }
        val listOfWorkers = mutableListOf<WorkerProfile>()
        val items = AWSHelperFunctions().AWSQuerry(request)
        if (items != null) {
            for (worker in items) {
                listOfWorkers.add(
                    WorkerAWSMapsToObjects().dynamoMapToWorkerProfile(worker)
                )
            }
        }
        return listOfWorkers
    }

    override suspend fun deleteAccount(email: String) {

        val attrValues = mutableMapOf<String, AttributeValue>()
        attrValues[":partitionKey"] = AttributeValue.S(email)

        val queryRequest = QueryRequest {
            tableName = Constants.tableName
            keyConditionExpression = "partitionKey = :partitionKey"
            this.expressionAttributeValues = attrValues
        }
        DynamoDbClient { region = "ap-southeast-2" }.use { db ->
            val items = db.query(queryRequest).items
            if (items != null) {
                for (item in items) {
                    try {
                        val keyToGet = mutableMapOf<String, AttributeValue>()
                        keyToGet["partitionKey"] = AttributeValue.S(email)
                        keyToGet["SortKey"] = item["SortKey"]?.let { AttributeValue.S(it.asS()) }!!
                        val request = DeleteItemRequest {
                            key = keyToGet
                            tableName = Constants.tableName
                        }
                        DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                            db.deleteItem(request)
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }
}