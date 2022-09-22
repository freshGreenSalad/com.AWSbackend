package com.example.awsServices.dynamoDb.Employee

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WorkerProfileDynamoDBDataSource(
) : WorkerProfileDynamoDBInterface {

    //aws visualiser route functions for workers
    // putWorkerSignupInfo
    // putWorkerSiteInfo
    // putWorkerSpecialLicence
    // putDatesWorked
    // putWorkerPersonalData
    // putWorkerExperience
    override suspend fun putWorkerSignupInfo(email: String, password: String, salt: String):Boolean  {
        val itemValues = mutableMapOf<String, AttributeValue>()

        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("signIn")
        itemValues["password"] = AttributeValue.S(password)
        itemValues["salt"] = AttributeValue.S(salt)
        itemValues["isSupervisor"] = AttributeValue.Bool(false)

        val request = PutItemRequest {
            tableName = "workerAppTable"
            item = itemValues
        }

        return try {
            DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                db.putItem(request)

            }
            true

        } catch (e: Exception) {
            false
        }
    }

    override suspend fun putWorkerSiteInfo(
        email: String,
        address: String,
        siteExpliation: String,
        siteAddressExplination: String,
        googleMapsLocation: String,
        siteDaysWorkedAndThereUsualStartAndEndTime: String,
        terrain: String,
        sitePhoto: String
    ){

        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("Site")
        itemValues["siteAddress"] = AttributeValue.S(address)
        itemValues["siteExplination"] = AttributeValue.S(siteExpliation)
        itemValues["siteAddressExplination"] = AttributeValue.S(siteAddressExplination)
        itemValues["googleMapsLocation"] = AttributeValue.S(googleMapsLocation)
        itemValues["siteDaysWorkedAndThereUsualStartAndEndTime"] = AttributeValue.S(siteDaysWorkedAndThereUsualStartAndEndTime)
        itemValues["terrain"] = AttributeValue.S(terrain)
        itemValues["sitePhoto"] = AttributeValue.S(sitePhoto)

        val request = PutItemRequest {
            tableName = "workerAppTable"
            item = itemValues
        }

       try {
            DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                db.putItem(request)
            }

        } catch (e: Exception) {

        }
    }

    override suspend fun putWorkerSpecialLicence(
        email: String,
        licenceType: String,
        issueDate: String,
        expireyDate: String,
        licencePhoto: String
    ) {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("Licence#$licenceType")
        itemValues["licenceType"] = AttributeValue.S(licenceType)
        itemValues["issueDate"] = AttributeValue.S(issueDate)
        itemValues["expireyDate"] = AttributeValue.S(expireyDate)
        itemValues["licencePhoto"] = AttributeValue.S(licencePhoto)

        val request = PutItemRequest {
            tableName = "workerAppTable"
            item = itemValues
        }

        try {
            DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                db.putItem(request)
            }

        } catch (e: Exception) {

        }
    }

    override suspend fun putDatesWorked(
        email: String,
        aggregate: String,
        jan: String,
        feb: String,
        march: String,
        april: String,
        may: String,
        june: String,
        july: String,
        august: String,
        september: String,
        october: String,
        november: String,
        december: String
    ) {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("datesWorked")
        itemValues["jan"] = AttributeValue.S(jan)
        itemValues["feb"] = AttributeValue.S(feb)
        itemValues["march"] = AttributeValue.S(march)
        itemValues["april"] = AttributeValue.S(april)
        itemValues["may"] = AttributeValue.S(may)
        itemValues["june"] = AttributeValue.S(june)
        itemValues["july"] = AttributeValue.S(july)
        itemValues["august"] = AttributeValue.S(august)
        itemValues["september"] = AttributeValue.S(september)
        itemValues["october"] = AttributeValue.S(october)
        itemValues["november"] = AttributeValue.S(november)
        itemValues["december"] = AttributeValue.S(december)

        val request = PutItemRequest {
            tableName = "workerAppTable"
            item = itemValues
        }

        try {
            DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                db.putItem(request)
            }

        } catch (e: Exception) {

        }
    }

    override suspend fun putWorkerPersonalData(
        email: String,
        supervisor: String,
        firstname: String,
        lastname: String,
        recordOfAttendance: String,
        rate: String,
        personalPhoto: String
    ) {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("personal")
        itemValues["supervisor"] = AttributeValue.S(supervisor)
        itemValues["firstname"] = AttributeValue.S(firstname)
        itemValues["lastname"] = AttributeValue.S(lastname)
        itemValues["recordOfAttendance"] = AttributeValue.S(recordOfAttendance)
        itemValues["rate"] = AttributeValue.S(rate)
        itemValues["personalPhoto"] = AttributeValue.S(personalPhoto)

        val request = PutItemRequest {
            tableName = "workerAppTable"
            item = itemValues
        }

        try {
            DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                db.putItem(request)
            }

        } catch (e: Exception) {

        }
    }

    override suspend fun putWorkerExperience(
        email: String,
        typeofExperience: String,
        ratingAggregate: String,
        previousratingsfromSupervisors: String
    ) {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("experience#$typeofExperience")
        itemValues["ratingAggregate"] = AttributeValue.S(typeofExperience)
        itemValues["previousratingsfromSupervisors"] = AttributeValue.S(previousratingsfromSupervisors)

        val request = PutItemRequest {
            tableName = "workerAppTable"
            item = itemValues
        }

        try {
            DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                db.putItem(request)
            }

        } catch (e: Exception) {

        }
    }

}