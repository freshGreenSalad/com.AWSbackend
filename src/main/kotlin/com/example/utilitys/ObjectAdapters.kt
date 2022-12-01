package com.example.utilitys

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.example.Data.models.Auth.AuthRequest
import com.example.Data.models.Auth.AuthRequestWithIsSupervisor
import com.example.Data.models.DriversLicence
import com.example.Data.models.general.Location
import com.example.Data.models.supervisorVisualiser.SupervisorProfile
import com.example.Data.models.supervisorVisualiser.SupervisorSite
import com.example.Data.models.workerVisualiser.*
import com.plcoding.security.hashing.SaltedHash

class ObjectAdapters {

    fun MapWorkerLicenceToItemValues(licence: DriversLicence): MutableMap<String, AttributeValue> {
        val itemValues = mutableMapOf<String, AttributeValue>()

        val licenceMap = mutableMapOf<String, AttributeValue>()
        licence.licenceMap.map { licenceMap.put(key = it.key, value = AttributeValue.Bool(it.value)) }

        itemValues["partitionKey"] = AttributeValue.S(licence.email)
        itemValues["SortKey"] = AttributeValue.S("driversLicence")
        itemValues["typeOfDriversLicence"] = AttributeValue.S(licence.typeOfLicence.name)
        itemValues["driversLicenceMap"] = AttributeValue.M(licenceMap)
        itemValues["highestClass"] = AttributeValue.S(licence.highestClass.name)
        return itemValues
    }

    fun WorkerSiteToItemValuesMap(workerSite: WorkerSite): MutableMap<String, AttributeValue> {
        val itemValues = mutableMapOf<String, AttributeValue>()

        itemValues["partitionKey"] = AttributeValue.S(workerSite.email)
        itemValues["SortKey"] = AttributeValue.S("site")
        itemValues["siteAddress"] = AttributeValue.S(workerSite.address)
        itemValues["siteExplanation"] = AttributeValue.S(workerSite.siteExplanation)
        itemValues["siteAddressExplanation"] = AttributeValue.S(workerSite.siteAddressExplanation)
        itemValues["googleMapsLocation"] = AttributeValue.S(workerSite.googleMapsLocation)
        itemValues["siteDaysWorkedAndThereUsualStartAndEndTime"] = AttributeValue.S(workerSite.siteDaysWorkedAndThereUsualStartAndEndTime)
        itemValues["terrain"] = AttributeValue.S(workerSite.terrain)
        itemValues["sitePhoto"] = AttributeValue.S(workerSite.sitePhoto)
        return itemValues
    }

    fun WorkerExperienceToItemValues(
        experience: Experience
    ): MutableMap<String, AttributeValue> {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(experience.email)
        itemValues["SortKey"] = AttributeValue.S("experience#${experience.typeofExperience}")
        itemValues["typeofExperience"] = AttributeValue.S(experience.typeofExperience)
        itemValues["ratingAggregate"] = AttributeValue.S(experience.ratingAggregate)
        itemValues["previousRatingsFromSupervisors"] = AttributeValue.S(experience.previousRatingsFromSupervisors)
        return itemValues
    }

    fun workerProfileToItemValues(workerProfile: WorkerProfile): MutableMap<String, AttributeValue> {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(workerProfile.email)
        itemValues["SortKey"] = AttributeValue.S("personal#worker")
        itemValues["firstname"] = AttributeValue.S(workerProfile.firstName)
        itemValues["lastname"] = AttributeValue.S(workerProfile.lastName)
        itemValues["rate"] = AttributeValue.N(workerProfile.rate.toString())
        itemValues["personalPhoto"] = AttributeValue.S(workerProfile.personalPhoto)
        return itemValues
    }

    fun DatesWorkedToItemValues(datesWorkerd: DatesWorked): MutableMap<String, AttributeValue> {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(datesWorkerd.email)
        itemValues["SortKey"] = AttributeValue.S("datesWorked")
        itemValues["recordOfAttendance"] = AttributeValue.S("98")
        itemValues["jan"] = AttributeValue.S(datesWorkerd.jan)
        itemValues["feb"] = AttributeValue.S(datesWorkerd.feb)
        itemValues["march"] = AttributeValue.S(datesWorkerd.march)
        itemValues["april"] = AttributeValue.S(datesWorkerd.april)
        itemValues["may"] = AttributeValue.S(datesWorkerd.may)
        itemValues["june"] = AttributeValue.S(datesWorkerd.june)
        itemValues["july"] = AttributeValue.S(datesWorkerd.july)
        itemValues["august"] = AttributeValue.S(datesWorkerd.august)
        itemValues["september"] = AttributeValue.S(datesWorkerd.september)
        itemValues["october"] = AttributeValue.S(datesWorkerd.october)
        itemValues["november"] = AttributeValue.S(datesWorkerd.november)
        itemValues["december"] = AttributeValue.S(datesWorkerd.december)
        return itemValues
    }

    fun specialLicenceToItemValues(specialLicence: SpecialLicence): MutableMap<String, AttributeValue> {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(specialLicence.email)
        itemValues["SortKey"] = AttributeValue.S("licence#${specialLicence.licenceType}")
        itemValues["licenceType"] = AttributeValue.S(specialLicence.licenceType)
        itemValues["issueDate"] = AttributeValue.S(specialLicence.issueDate)
        itemValues["expiryDate"] = AttributeValue.S(specialLicence.expiryDate)
        itemValues["licencePhoto"] = AttributeValue.S(specialLicence.licencePhoto)
        return itemValues
    }

    /////////
    fun SignupInfoToItemValues(
        WorkerSignupInfo: AuthRequestWithIsSupervisor,
        saltedHash: SaltedHash
    ): MutableMap<String, AttributeValue> {
        val itemValues = mutableMapOf<String, AttributeValue>()

        itemValues["partitionKey"] = AttributeValue.S(WorkerSignupInfo.email)
        itemValues["SortKey"] = AttributeValue.S("signIn")
        itemValues["password"] = AttributeValue.S(saltedHash.hash)
        itemValues["salt"] = AttributeValue.S(saltedHash.salt)
        itemValues["isSupervisor"] = AttributeValue.Bool(WorkerSignupInfo.isSupervisor)
        return itemValues
    }

    private fun googleMapsLocationToMap(googleMapsLocation: Location): Map<String, AttributeValue.N> {
        val latlng = mapOf(
            "lat" to AttributeValue.N(googleMapsLocation.Lat.toString()),
            "lng" to AttributeValue.N(googleMapsLocation.Lng.toString())
        )
        return latlng
    }

    fun SiteAddressToItemValues(supervisorSite: SupervisorSite): MutableMap<String, AttributeValue> {
        val itemValues = mutableMapOf<String, AttributeValue>()
        val latlng = googleMapsLocationToMap(supervisorSite.location)
        itemValues["partitionKey"] = AttributeValue.S(supervisorSite.email)
        itemValues["SortKey"] = AttributeValue.S("site")
        itemValues["siteAddress"] = AttributeValue.S(supervisorSite.address)
        itemValues["googleMapsLocation"] = AttributeValue.M(latlng)
        return itemValues
    }

    fun supervisorProfiletoItemvalues(supervisorProfile: SupervisorProfile): MutableMap<String, AttributeValue> {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues["partitionKey"] = AttributeValue.S(supervisorProfile.email)
        itemValues["SortKey"] = AttributeValue.S("personal")
        itemValues["firstname"] = AttributeValue.S(supervisorProfile.firstName)
        itemValues["lastname"] = AttributeValue.S(supervisorProfile.lastName)
        itemValues["personalPhoto"] = AttributeValue.S(supervisorProfile.personalPhoto)
        return itemValues
    }

}