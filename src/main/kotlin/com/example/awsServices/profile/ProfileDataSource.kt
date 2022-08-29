package com.example.awsServices.profile

import com.example.awsServices.profile.Profile

interface ProfileDataSource {

    suspend fun getProfileByEmail(email:String): Profile

    suspend fun putKeyValueInTable(profile: Profile)
}