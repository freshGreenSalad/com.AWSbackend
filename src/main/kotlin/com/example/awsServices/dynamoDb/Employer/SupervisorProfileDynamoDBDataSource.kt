package com.example.awsServices.dynamoDb.Employer

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.example.Data.models.SupervisorProfileDynamoDBInterface
import com.example.Data.models.workerVisualiser.Personal
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
        address: String,
        siteExpliation: String,
        siteAddressExplination: String,
        googleMapsLocation: String,
        siteDaysWorkedAndThereUsualStartAndEndTime: String,
        terrain: String,
        sitePhoto: String
    ) {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("Site")
        itemValues["siteAddress"] = AttributeValue.S(address)
        itemValues["siteExplanation"] = AttributeValue.S(siteExpliation)
        itemValues["siteAddressExplanation"] = AttributeValue.S(siteAddressExplination)
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

    override suspend fun putSupervisorPersonalData(
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
            val salt = item?.get("salt").toString()
            val password = item?.get("password").toString()
            val authSaltPassword = AuthSaltPasswordEmail(
                email = email,
                password = password,
                salt = salt,
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
            val email = item?.get("salt").toString()
            val address = item?.get("address").toString()
            val siteExplanation = item?.get("siteExplanation").toString()
            val siteAddressExplanation = item?.get("siteAddressExplanation").toString()
            val googleMapsLocation = item?.get("googleMapsLocation").toString()
            val siteDaysWorkedAndThereUsualStartAndEndTime =
                item?.get("siteDaysWorkedAndThereUsualStartAndEndTime").toString()
            val terrain = item?.get("terrain").toString()
            val sitePhoto = item?.get("sitePhoto").toString()
            val workerSite = WorkerSite(
                email = email,
                address = address,
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

    override suspend fun getSupervisorPersonalData(email: String): AwsResultWrapper<Personal> {
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
            val email = item?.get("email").toString()
            val supervisor = item?.get("supervisor").toString()
            val firstname = item?.get("firstname").toString()
            val lastname = item?.get("lastname").toString()
            val recordOfAttendance = item?.get("recordOfAttendance").toString()
            val rate = item?.get("rate").toString()
            val personalPhoto = item?.get("personalPhoto").toString()
            val personal = Personal(
                email = email,
                supervisor = supervisor,
                firstname = firstname,
                lastname = lastname,
                recordOfAttendance = recordOfAttendance,
                rate = rate,
                personalPhoto = personalPhoto
            )

            return AwsResultWrapper.Success(data = personal)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }
}

