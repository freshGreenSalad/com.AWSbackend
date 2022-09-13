package com.example.Data.RoutingInterfaces

import com.example.Data.models.Profile
import com.example.Data.models.ProfileInformation
import com.example.Data.models.WorkerProfileDataClass

interface WorkerProfileDynamoDBInterface {
    suspend fun getProfileByEmail(email: String): Profile

    suspend fun putWorkerProfileInDB(WorkerProfile: WorkerProfileDataClass):Boolean

    suspend fun updateTableItem(email:String, profileInformation: ProfileInformation)
}