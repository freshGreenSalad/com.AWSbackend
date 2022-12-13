package com.example.utilitys

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import com.example.UserPathways.LoginSignup.Auth.SaltPasswordEmailIsSupervisor
import com.example.Data.models.DriversLicence
import com.example.Data.models.HighestClass
import com.example.Data.models.TypeOfLicence
import com.example.UserPathways.Employee.workerVisualiser.*
import com.example.UserPathways.employer.supervisorVisualiser.Location
import com.example.UserPathways.employer.supervisorVisualiser.SupervisorProfile
import com.example.UserPathways.employer.supervisorVisualiser.SupervisorSite

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
    suspend fun dynamoResultToDriverslicence(request: GetItemRequest, email: String): SaltPasswordEmailIsSupervisor {
        val result = AWSHelperFunctions().GetItem(request)!!
        return SaltPasswordEmailIsSupervisor(
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
    fun dynamoMapToWorkerProfile(item: Map<String, AttributeValue>): WorkerProfile {
        return try {
        return WorkerProfile(
            email = item["partitionKey"]?.asS() ?: "",
            firstName = item["firstname"]?.asS() ?: "",
            lastName = item["lastname"]?.asS() ?: "",
            personalPhoto = item["personalPhoto"]?.asS() ?: "",
            rate = item["rate"]?.asN()?.toInt() ?: 0,
        )
        }catch(e:Exception){
            WorkerProfile("","","","",2345)
        }
    }
    fun dynamoResultToExperience(item: Map<String, AttributeValue>): Experience {
        return Experience(
            experience = item["partitionKey"]?.asS().toString().split("#").last(),
            years = item["SortKey"]?.asS().toString().toInt(),
            email = item["ratingAggregate"]?.asS().toString(),
        )
    }
    fun dynamoResultToSpecialLicence(item: Map<String, AttributeValue>): SpecialLicence {
        return SpecialLicence(
            email = item["partitionKey"]?.asS().toString(),
            licenceType = item["licenceType"]?.asS().toString(),
            issueDate = item["issueDate"]?.asS().toString(),
            expiryDate = item["expiryDate"]?.asS().toString(),
            licencePhoto = item["licencePhoto"]?.asS().toString()
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
    suspend fun dynamoResultToSupervisorSite(request: GetItemRequest): SupervisorSite {
        val result = AWSHelperFunctions().GetItem(request)!!
        val latlng = result["googleMapsLocation"]?.asM()!!
        return SupervisorSite(
            email = result["partitionKey"]?.asS().toString(),
            address = result["siteAddress"]?.asS().toString(),
            location = Location(Lat = latlng["lat"]?.asN()!!.toDouble(), Lng = latlng["lng"]?.asN()!!.toDouble() )
        )
    }
}