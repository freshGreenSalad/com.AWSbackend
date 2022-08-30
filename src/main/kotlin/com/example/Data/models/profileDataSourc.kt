package com.example.Data.models


interface ProfileDataSourceInterface {

    suspend fun getProfileByEmail(email:String):Profile

    suspend fun insertUser(user: Profile):Boolean
}