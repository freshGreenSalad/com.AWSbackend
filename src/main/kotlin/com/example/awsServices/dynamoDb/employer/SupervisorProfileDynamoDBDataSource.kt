package com.example.awsServices.dynamoDb.employer

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.example.Data.models.SupervisorProfileDynamoDBInterface
import com.example.Data.models.general.Location
import com.example.Data.models.supervisorVisualiser.SupervisorProfile
import com.example.Data.models.workerVisualiser.WorkerSite
import com.example.Data.wrapperClasses.AwsResultWrapper

class SupervisorProfileDynamoDBDataSource(
) : SupervisorProfileDynamoDBInterface {

    //aws visualiser route functions for Supervisors
    // putSupervisorSignupInfo
    // putSupervisorSiteInfo
    // putSupervisorPersonalData
    override suspend fun putSupervisorSignupInfo(email: String, password: String, salt: String): Boolean {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("signIn")
        itemValues["password"] = AttributeValue.S(password)
        itemValues["salt"] = AttributeValue.S(salt)
        itemValues["isSupervisor"] = AttributeValue.Bool(true)

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

    override suspend fun putSupervisorSiteInfo(
        email: String,
        siteAddress: String,
        //siteExpliation: String,
        //siteAddressExplination: String,
        googleMapsLocation: Location,
        //siteDaysWorkedAndThereUsualStartAndEndTime: String,
        //terrain: String,
        //sitePhoto: String
    ) {

        val latlng = mapOf(
            "lat" to AttributeValue.N(googleMapsLocation.Lat.toString()),
            "lng" to AttributeValue.N(googleMapsLocation.Lng.toString())
        )

        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("site")
        itemValues["siteAddress"] = AttributeValue.S(siteAddress)
        //itemValues["siteExplanation"] = AttributeValue.S(siteExpliation)
        //itemValues["siteAddressExplanation"] = AttributeValue.S(siteAddressExplination)
        itemValues["googleMapsLocation"] = AttributeValue.M(latlng)
        //itemValues["siteDaysWorkedAndThereUsualStartAndEndTime"] = AttributeValue.S(siteDaysWorkedAndThereUsualStartAndEndTime)
        //itemValues["terrain"] = AttributeValue.S(terrain)
        //itemValues["sitePhoto"] = AttributeValue.S(sitePhoto)

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

    override suspend fun putSupervisorPersonalData(
        email: String,
        firstname: String,
        lastname: String,
        personalPhoto: String
    ) {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("personal")
        itemValues["firstname"] = AttributeValue.S(firstname)
        itemValues["lastname"] = AttributeValue.S(lastname)
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

    override suspend fun getSupervisorSignupAuth(email: String): AwsResultWrapper<AuthSaltPasswordEmail> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["SortKey"] = AttributeValue.S("signIn")


        val request = GetItemRequest {
            key = keyToGet
            tableName = "workerAppTable"
        }
        return try {
            val result = DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                db.getItem(request)
            }
            val item = result.item
            val salt = item?.get("salt")?.asS().toString()
            val password = item?.get("password")?.asS().toString()
            val authSaltPassword = AuthSaltPasswordEmail(
                email = email,
                password = password,
                salt = salt,
                isSupervisor = true
            )

            return AwsResultWrapper.Success(data = authSaltPassword)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getSupervisorSiteInfo(email: String): AwsResultWrapper<WorkerSite> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["SortKey"] = AttributeValue.S("site")

        val request = GetItemRequest {
            key = keyToGet
            tableName = "workerAppTable"
        }
        return try {
            val result = DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                db.getItem(request)
            }
            val item = result.item
            val itemEmail = item?.get("partitionKey")?.asS().toString()
            val siteAddress = item?.get("siteAddress")?.asS().toString()
            val siteExplanation = item?.get("siteExplanation")?.asS().toString()
            val siteAddressExplanation = item?.get("siteAddressExplanation")?.asS().toString()
            val googleMapsLocation = item?.get("googleMapsLocation")?.asS().toString()
            val siteDaysWorkedAndThereUsualStartAndEndTime =
                item?.get("siteDaysWorkedAndThereUsualStartAndEndTime")?.asS().toString()
            val terrain = item?.get("terrain")?.asS().toString()
            val sitePhoto = item?.get("sitePhoto")?.asS().toString()
            val workerSite = WorkerSite(
                email = itemEmail,
                address = siteAddress,
                siteExplanation = siteExplanation,
                siteAddressExplanation = siteAddressExplanation,
                googleMapsLocation = googleMapsLocation,
                siteDaysWorkedAndThereUsualStartAndEndTime = siteDaysWorkedAndThereUsualStartAndEndTime,
                terrain = terrain,
                sitePhoto = sitePhoto
            )

            return AwsResultWrapper.Success(data = workerSite)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getSupervisorPersonalData(email: String): AwsResultWrapper<SupervisorProfile> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["SortKey"] = AttributeValue.S("personal")


        val request = GetItemRequest {
            key = keyToGet
            tableName = "workerAppTable"
        }
        return try {
            val result = DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                db.getItem(request)
            }
            val item = result.item
            val itemEmail = item?.get("partitionKey")?.asS().toString()
            val firstname = item?.get("firstname")?.asS().toString()
            val lastname = item?.get("lastname")?.asS().toString()
            val personalPhoto = item?.get("personalPhoto")?.asS().toString()
            val personal = SupervisorProfile(
                email = itemEmail,
                firstName = firstname,
                lastName = lastname,
                personalPhoto = personalPhoto
            )

            return AwsResultWrapper.Success(data = personal)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }
}
