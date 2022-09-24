package com.example.awsServices.dynamoDb.Employee

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.example.Data.models.workerVisualiser.*
import com.example.Data.wrapperClasses.AwsResultWrapper

class WorkerProfileDynamoDBDataSource(
) : WorkerProfileDynamoDBInterface {

    //aws visualiser route functions for workers
    // putWorkerSignupInfo
    // putWorkerSiteInfo
    // putWorkerSpecialLicence
    // putDatesWorked
    // putWorkerPersonalData
    // putWorkerExperience
    override suspend fun putWorkerSignupInfo(email: String, password: String, salt: String): Boolean {
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
    ) {

        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("Site")
        itemValues["siteAddress"] = AttributeValue.S(address)
        itemValues["siteExplanation"] = AttributeValue.S(siteExpliation)
        itemValues["siteAddressExplanation"] = AttributeValue.S(siteAddressExplination)
        itemValues["googleMapsLocation"] = AttributeValue.S(googleMapsLocation)
        itemValues["siteDaysWorkedAndThereUsualStartAndEndTime"] =
            AttributeValue.S(siteDaysWorkedAndThereUsualStartAndEndTime)
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
        itemValues["SortKey"] = AttributeValue.S("licence#$licenceType")
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
        previousRatingsFromSupervisors: String
    ) {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("experience#$typeofExperience")
        itemValues["ratingAggregate"] = AttributeValue.S(typeofExperience)
        itemValues["previousRatingsFromSupervisors"] = AttributeValue.S(previousRatingsFromSupervisors)

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
    //aws visualiser route functions for workers
    // getWorkerSignupAuth
    // getWorkerSiteInfo
    // getWorkerSpecialLicence
    // getDatesWorked
    // getWorkerPersonalData
    // getWorkerExperience

    override suspend fun getWorkerSignupAuth(email: String): AwsResultWrapper<AuthSaltPasswordEmail> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["sortKey"] = AttributeValue.S("signIn")


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

    override suspend fun getWorkerSiteInfo(email: String): AwsResultWrapper<WorkerSite> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["sortKey"] = AttributeValue.S("site")


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
            val siteDaysWorkedAndThereUsualStartAndEndTime = item?.get("siteDaysWorkedAndThereUsualStartAndEndTime").toString()
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

    override suspend fun getWorkerSpecialLicence(email: String): AwsResultWrapper<SpecialLicence> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["SortKey"] = AttributeValue.S("licence")


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
            val licenceType = item?.get("licenceType").toString()
            val issueDate = item?.get("issueDate").toString()
            val expiryDate = item?.get("expiryDate").toString()
            val licencePhoto = item?.get("licencePhoto").toString()
            val specialLicence = SpecialLicence(
                email = email,
                licenceType = licenceType,
                issueDate = issueDate,
                expiryDate = expiryDate,
                licencePhoto = licencePhoto
            )

            return AwsResultWrapper.Success(data = specialLicence)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getDatesWorked(email: String): AwsResultWrapper<DatesWorked> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["SortKey"] = AttributeValue.S("datesWorked")


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
            val aggregate = item?.get("aggregate").toString()
            val jan = item?.get("jan").toString()
            val feb = item?.get("feb").toString()
            val march = item?.get("march").toString()
            val april = item?.get("april").toString()
            val may = item?.get("may").toString()
            val june = item?.get("june").toString()
            val july = item?.get("july").toString()
            val august = item?.get("august").toString()
            val september = item?.get("september").toString()
            val october = item?.get("october").toString()
            val november = item?.get("november").toString()
            val december = item?.get("december").toString()
            val datesWorked = DatesWorked(
                email = email,
                aggregate = aggregate,
                jan = jan,
                feb = feb,
                march = march,
                april = april,
                may = may,
                june = june,
                july = july,
                august = august,
                september = september,
                october = october,
                november = november,
                december = december
            )

            return AwsResultWrapper.Success(data = datesWorked)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getWorkerPersonalData(email: String): AwsResultWrapper<Personal> {
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

    override suspend fun getWorkerExperience(email: String): AwsResultWrapper<Experience> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["SortKey"] = AttributeValue.S("experience")


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
            val typeofExperience = item?.get("typeofExperience").toString()
            val ratingAggregate = item?.get("ratingAggregate").toString()
            val previousRatingsFromSupervisors = item?.get("previousRatingsFromSupervisors").toString()
            val experience = Experience(
                email = email,
                typeofExperience = typeofExperience,
                ratingAggregate = ratingAggregate,
                previousratingsfromSupervisors = previousRatingsFromSupervisors
            )

            return AwsResultWrapper.Success(data = experience)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }
}