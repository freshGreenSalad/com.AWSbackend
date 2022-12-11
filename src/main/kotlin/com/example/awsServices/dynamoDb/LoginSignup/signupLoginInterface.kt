package com.example.awsServices.dynamoDb.LoginSignup

import com.example.Data.models.Auth.AuthRequestWithIsSupervisor
import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.example.Data.wrapperClasses.AwsResultWrapper
import com.plcoding.security.hashing.SaltedHash

interface signupLoginInterface {
    suspend fun login(email: String): AwsResultWrapper<AuthSaltPasswordEmail>
    suspend fun signup(Authrequest: AuthRequestWithIsSupervisor, saltedHash: SaltedHash): Boolean
}