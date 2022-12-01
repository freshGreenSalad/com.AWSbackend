package com.example.awsServices.dynamoDb.LoginSignup

import com.example.Data.models.Auth.AuthRequestWithIsSupervisor
import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.example.Data.wrapperClasses.AwsResultWrapper
import com.example.utilitys.AWSHelperFunctions
import com.example.utilitys.ObjectAdapters
import com.plcoding.security.hashing.SaltedHash

class SignupLoginDataSource():signupLoginInterface {
    override suspend fun login(email: String): AwsResultWrapper<AuthSaltPasswordEmail> {
        return try {
            val keyToGet = AWSHelperFunctions().KeyToGet(email, "signIn")
            val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
            val result = AWSHelperFunctions().GetItem(request)!!
            val authSaltPassword = AuthSaltPasswordEmail(
                email = email,
                password = result["password"]?.asS().toString(),
                salt = result["salt"]?.asS().toString(),
                isSupervisor = result["isSupervisor"]?.asBool()!!
            )
            return AwsResultWrapper.Success(data = authSaltPassword)
        } catch (e: Exception) {
            AwsResultWrapper.Fail()
        }
    }

    override suspend fun signup(Authrequest: AuthRequestWithIsSupervisor, saltedHash: SaltedHash): Boolean {
        return try{
            val itemValues = ObjectAdapters().SignupInfoToItemValues(Authrequest, saltedHash)
            val request = AWSHelperFunctions().buildPutItemRequest(itemValues)
            AWSHelperFunctions().PutObject(request)
            true
        } catch (e: Exception) {
            false
        }
    }
}