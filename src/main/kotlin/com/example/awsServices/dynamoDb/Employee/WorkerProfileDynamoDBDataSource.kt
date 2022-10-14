package com.example.awsServices.dynamoDb.Employee

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.example.Data.models.HighestClass
import com.example.Data.models.DriversLicence
import com.example.Data.models.TypeOfLicence
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
    override suspend fun putWorkerSignupInfo(
        email: String,
        password: String,
        salt: String,
        isSupervisor: Boolean
    ): Boolean {
        val itemValues = mutableMapOf<String, AttributeValue>()

        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("signIn")
        itemValues["password"] = AttributeValue.S(password)
        itemValues["salt"] = AttributeValue.S(salt)
        itemValues["isSupervisor"] = AttributeValue.Bool(isSupervisor)

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

    override suspend fun putWorkerDriversLicence(licence: DriversLicence) {
        val itemValues = mutableMapOf<String, AttributeValue>()

        val licenceMap = mutableMapOf<String, AttributeValue>()
        licence.licenceMap.map { licenceMap.put(key = it.key, value = AttributeValue.Bool(it.value)) }

        itemValues["partitionKey"] = AttributeValue.S(licence.email)
        itemValues["SortKey"] = AttributeValue.S("driversLicence")
        itemValues["typeOfDriversLicence"] = AttributeValue.S(licence.typeOfLicence.name)
        itemValues["driversLicenceMap"] = AttributeValue.M(licenceMap)
        itemValues["highestClass"] = AttributeValue.S(licence.highestClass.name)

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

    override suspend fun putWorkerSiteInfo(
        email: String,
        address: String,
        siteExplanation: String,
        siteAddressExplanation: String,
        googleMapsLocation: String,
        siteDaysWorkedAndThereUsualStartAndEndTime: String,
        terrain: String,
        sitePhoto: String
    ) {

        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("site")
        itemValues["siteAddress"] = AttributeValue.S(address)
        itemValues["siteExplanation"] = AttributeValue.S(siteExplanation)
        itemValues["siteAddressExplanation"] = AttributeValue.S(siteAddressExplanation)
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
        expiryDate: String,
        licencePhoto: String
    ) {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("licence#$licenceType")
        itemValues["licenceType"] = AttributeValue.S(licenceType)
        itemValues["issueDate"] = AttributeValue.S(issueDate)
        itemValues["expiryDate"] = AttributeValue.S(expiryDate)
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
        itemValues["recordOfAttendance"] = AttributeValue.S("98")
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
        firstname: String,
        lastname: String,
        rate: Int,
        personalPhoto: String
    ) {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(email)
        itemValues["SortKey"] = AttributeValue.S("personal#worker")
        itemValues["firstname"] = AttributeValue.S(firstname)
        itemValues["lastname"] = AttributeValue.S(lastname)
        itemValues["rate"] = AttributeValue.N(rate.toString())
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
        itemValues["typeofExperience"] = AttributeValue.S(typeofExperience)
        itemValues["ratingAggregate"] = AttributeValue.S(ratingAggregate)
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
            val isSupervisor = item?.get("isSupervisor")?.asBool() ?: false
            val authSaltPasswordEmail = AuthSaltPasswordEmail(
                email = email,
                password = password,
                salt = salt,
                isSupervisor = isSupervisor
            )

            return AwsResultWrapper.Success(data = authSaltPasswordEmail)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getWorkerDriversLicence(email: String): AwsResultWrapper<DriversLicence> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["SortKey"] = AttributeValue.S("driversLicence")

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
            val typeOfLicence = item?.get("typeOfDriversLicence")?.asS().toString()
            val licenceMap = mutableMapOf<String, Boolean>()
            item?.get("driversLicenceMap")?.asM()?.map { licenceMap.put(key = it.key, value = (it.value.asBool())) }

            val highestClass = item?.get("highestClass")?.asS().toString()
            val workerSite = DriversLicence(
                email = itemEmail,
                typeOfLicence = TypeOfLicence.valueOf(typeOfLicence),
                licenceMap = licenceMap,
                highestClass = HighestClass.valueOf(highestClass)
            )

            AwsResultWrapper.Success(data = workerSite)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getWorkerSiteInfo(email: String): AwsResultWrapper<WorkerSite> {
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
            val address = item?.get("siteAddress")?.asS().toString()
            val siteExplanation = item?.get("siteExplanation")?.asS().toString()
            val siteAddressExplanation = item?.get("siteAddressExplanation")?.asS().toString()
            val googleMapsLocation = item?.get("googleMapsLocation")?.asS().toString()
            val siteDaysWorkedAndThereUsualStartAndEndTime =
                item?.get("siteDaysWorkedAndThereUsualStartAndEndTime")?.asS().toString()
            val terrain = item?.get("terrain")?.asS().toString()
            val sitePhoto = item?.get("sitePhoto")?.asS().toString()
            val workerSite = WorkerSite(
                email = itemEmail,
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

    override suspend fun getWorkerSpecialLicence(email: String): AwsResultWrapper<MutableList<SpecialLicence>> {
        val attrValues = mutableMapOf<String, AttributeValue>()
        attrValues[":partitionKey"] = AttributeValue.S("email")
        attrValues[":SortKey"] = AttributeValue.S("licence")

        //leave the below expression alone change the values in the attribute value
        val request = QueryRequest {
            tableName = "workerAppTable"
            keyConditionExpression = "partitionKey = :partitionKey AND begins_with(SortKey, :SortKey)"
            this.expressionAttributeValues = attrValues
        }
        val listOfLicence = mutableListOf<SpecialLicence>()
        val result = DynamoDbClient { region = "ap-southeast-2" }.use { db ->
            db.query(request)
        }.items
        println(result)
        if (result != null) {
            for (item in result) {
                val itemEmail = item["partitionKey"]?.asS().toString()
                val licenceType = item["licenceType"]?.asS().toString()
                val issueDate = item["issueDate"]?.asS().toString()
                val expiryDate = item["expiryDate"]?.asS().toString()
                val licencePhoto = item["licencePhoto"]?.asS().toString()
                val specialLicence = SpecialLicence(
                    email = itemEmail,
                    licenceType = licenceType,
                    issueDate = issueDate,
                    expiryDate = expiryDate,
                    licencePhoto = licencePhoto
                )
                listOfLicence.add(specialLicence)
            }
        }

        return try {
            AwsResultWrapper.Success(data = listOfLicence)
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
            val itemEmail = item?.get("partitionKey")?.asS().toString()
            val recordOfAttendance = item?.get("recordOfAttendance")?.asS().toString()
            val jan = item?.get("jan")?.asS().toString()
            val feb = item?.get("feb")?.asS().toString()
            val march = item?.get("march")?.asS().toString()
            val april = item?.get("april")?.asS().toString()
            val may = item?.get("may")?.asS().toString()
            val june = item?.get("june")?.asS().toString()
            val july = item?.get("july")?.asS().toString()
            val august = item?.get("august")?.asS().toString()
            val september = item?.get("september")?.asS().toString()
            val october = item?.get("october")?.asS().toString()
            val november = item?.get("november")?.asS().toString()
            val december = item?.get("december")?.asS().toString()
            val datesWorked = DatesWorked(
                email = itemEmail,
                recordOfAttendance = recordOfAttendance,
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

    override suspend fun getWorkerPersonalData(email: String): AwsResultWrapper<WorkerProfile> {
        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["partitionKey"] = AttributeValue.S(email)
        keyToGet["SortKey"] = AttributeValue.S("personal#worker")


        val request = GetItemRequest {
            key = keyToGet
            tableName = "workerAppTable"
        }
        return try {
            val result = DynamoDbClient { region = "ap-southeast-2" }.use { db ->
                db.getItem(request)
            }
            println(result.toString())
            val item = result.item
            val itemEmail = item?.get("partitionKey")?.asS().toString()
            println(itemEmail)
            val firstname = item?.get("firstname")?.asS().toString()
            val lastname = item?.get("lastname")?.asS().toString()
            val rate = item?.get("rate")?.asN()?.toInt() ?: 0
            val personalPhoto = item?.get("personalPhoto")?.asS().toString()
            val personal = WorkerProfile(
                email = itemEmail,
                firstName = firstname,
                lastName = lastname,
                rate = rate,
                personalPhoto = personalPhoto
            )

            return AwsResultWrapper.Success(data = personal)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun getWorkerExperience(email: String): AwsResultWrapper<MutableList<Experience>> {

        val attrValues = mutableMapOf<String, AttributeValue>()
        attrValues[":partitionKey"] = AttributeValue.S("email")
        attrValues[":SortKey"] = AttributeValue.S("experience")

        //leave the below expression alone change the values in the attribute value
        val request = QueryRequest {
            tableName = "workerAppTable"
            keyConditionExpression = "partitionKey = :partitionKey AND begins_with(SortKey, :SortKey)"
            this.expressionAttributeValues = attrValues
        }
        val listOfExperience = mutableListOf<Experience>()
        val result = DynamoDbClient { region = "ap-southeast-2" }.use { db ->
            db.query(request)
        }.items
        println(result)
        if (result != null) {
            for (item in result) {
                val itemEmail = item["partitionKey"]?.asS().toString()
                val typeofExperience = item["typeofExperience"]?.asS().toString()
                val ratingAggregate = item["ratingAggregate"]?.asS().toString()
                val previousRatingsFromSupervisors = item["previousRatingsFromSupervisors"]?.asS().toString()
                val experience = Experience(
                    email = itemEmail,
                    typeofExperience = typeofExperience,
                    ratingAggregate = ratingAggregate,
                    previousRatingsFromSupervisors = previousRatingsFromSupervisors
                )
                listOfExperience.add(experience)
            }
        }

        return try {
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
            tableName = "workerAppTable"
            indexName = "reverseLookup"
            keyConditionExpression = "SortKey = :SortKey"// AND partitionKey = :partitionKey"
            this.expressionAttributeValues = attrValues
        }

        val listOfWorkers = mutableListOf<WorkerProfile>()

        DynamoDbClient { region = "ap-southeast-2" }.use { db ->
            val items = db.query(request).items
            if (items != null) {
                for (worker in items) {
                    listOfWorkers.add(
                        WorkerProfile(
                            email = worker["partitionKey"]?.asS() ?: "",
                            firstName = worker["firstname"]?.asS() ?: "",
                            lastName = worker["lastname"]?.asS() ?: "",
                            personalPhoto = worker["personalPhoto"]?.asS() ?: "",
                            rate = worker["rate"]?.asN()?.toInt() ?: 0,
                        )
                    )
                }
            }
        }
        return listOfWorkers
    }

    override suspend fun deleteAccount(email: String) {

        val attrValues = mutableMapOf<String, AttributeValue>()
        attrValues[":partitionKey"] = AttributeValue.S(email)

        val queryRequest = QueryRequest {
            tableName = "workerAppTable"
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
                            tableName = "workerAppTable"
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