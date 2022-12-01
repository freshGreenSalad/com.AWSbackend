package com.example.utilitys

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.example.Data.models.DriversLicence
import com.example.Data.models.HighestClass
import com.example.Data.models.TypeOfLicence
import com.example.Data.models.supervisorVisualiser.SupervisorProfile
import com.example.Data.models.workerVisualiser.*

class WorkerDynamoObjectConverters {

    suspend fun dynamoResultToWorkerSite(request: GetItemRequest): WorkerSite {
        val result = AWSHelperFunctions().GetItem(request)!!
        return WorkerSite(
            email = result["partitionKey"]?.asS().toString(),
            address = result["siteAddress"]?.asS().toString(),
            siteExplanation = result["siteExplanation"]?.asS().toString(),
            siteAddressExplanation = result["siteAddressExplanation"]?.asS().toString(),
            googleMapsLocation = result["googleMapsLocation"]?.asS().toString(),
            siteDaysWorkedAndThereUsualStartAndEndTime = result["siteDaysWorkedAndThereUsualStartAndEndTime"]?.asS()
                .toString(),
            terrain = result["terrain"]?.asS().toString(),
            sitePhoto = result["sitePhoto"]?.asS().toString()
        )
    }

    suspend fun dynamoResultToDriverslicence(request: GetItemRequest): DriversLicence {
        val result = AWSHelperFunctions().GetItem(request)!!
        val licenceMap = mutableMapOf<String, Boolean>()
        result["driversLicenceMap"]?.asM()?.map { licenceMap.put(key = it.key, value = (it.value.asBool())) }
        return DriversLicence(
            email = result["partitionKey"]?.asS().toString(),
            typeOfLicence = TypeOfLicence.valueOf(result["typeOfDriversLicence"]?.asS().toString()),
            licenceMap = licenceMap,
            highestClass = HighestClass.valueOf(result["highestClass"]?.asS().toString())
        )
    }

    suspend fun dynamoResultToDriverslicence(
        request: GetItemRequest,
        email: String
    ): AuthSaltPasswordEmail {
        val result = AWSHelperFunctions().GetItem(request)!!
        return AuthSaltPasswordEmail(
            email = email,
            password = result["password"]?.asS().toString(),
            salt = result["salt"]?.asS().toString(),
            isSupervisor = result["isSupervisor"]?.asBool() ?: false
        )
    }

    suspend fun dynamoResultToDatesWorked(request: GetItemRequest): DatesWorked {
        val result = AWSHelperFunctions().GetItem(request)!!
        return DatesWorked(
            email = result["partitionKey"]?.asS().toString(),
            recordOfAttendance = result["recordOfAttendance"]?.asS().toString(),
            jan = result["jan"]?.asS().toString(),
            feb = result["feb"]?.asS().toString(),
            march = result["march"]?.asS().toString(),
            april = result["april"]?.asS().toString(),
            may = result["may"]?.asS().toString(),
            june = result["june"]?.asS().toString(),
            july = result["july"]?.asS().toString(),
            august = result["august"]?.asS().toString(),
            september = result["september"]?.asS().toString(),
            october = result["october"]?.asS().toString(),
            november = result["november"]?.asS().toString(),
            december = result["december"]?.asS().toString()
        )
    }

    suspend fun dynamoResultToPersonalData(request: GetItemRequest): WorkerProfile {
        val result = AWSHelperFunctions().GetItem(request)!!
        return dynamoMapToWorkerProfile(result)
    }

    fun dynamoResultToExperience(item: Map<String, AttributeValue>): Experience {
        return Experience(
            email = item["partitionKey"]?.asS().toString(),
            typeofExperience = item["typeofExperience"]?.asS().toString(),
            ratingAggregate = item["ratingAggregate"]?.asS().toString(),
            previousRatingsFromSupervisors = item["previousRatingsFromSupervisors"]?.asS().toString()
        )
    }

    fun dynamoResultToSpecialLicence(item: Map<String, AttributeValue>): SpecialLicence {
        val specialLicence = SpecialLicence(
            email = item["partitionKey"]?.asS().toString(),
            licenceType = item["licenceType"]?.asS().toString(),
            issueDate = item["issueDate"]?.asS().toString(),
            expiryDate = item["expiryDate"]?.asS().toString(),
            licencePhoto = item["licencePhoto"]?.asS().toString()
        )
        return specialLicence
    }

    fun dynamoMapToWorkerProfile(worker: Map<String, AttributeValue>):WorkerProfile {
        return WorkerProfile(
            email = worker["partitionKey"]?.asS() ?: "",
            firstName = worker["firstname"]?.asS() ?: "",
            lastName = worker["lastname"]?.asS() ?: "",
            personalPhoto = worker["personalPhoto"]?.asS() ?: "",
            rate = worker["rate"]?.asN()?.toInt() ?: 0,
        )
    }

    suspend fun dynamoResultTosupervisorProfile(request: GetItemRequest): SupervisorProfile {
        val result = AWSHelperFunctions().GetItem(request)!!
        val personal = SupervisorProfile(
            email = result["partitionKey"]?.asS().toString(),
            firstName = result["firstname"]?.asS().toString(),
            lastName = result["lastname"]?.asS().toString(),
            personalPhoto = result["personalPhoto"]?.asS().toString()
        )
        return personal
    }
}