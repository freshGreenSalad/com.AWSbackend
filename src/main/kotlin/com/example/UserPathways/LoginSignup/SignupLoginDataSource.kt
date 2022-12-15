package com.example.UserPathways.LoginSignup

import com.example.UserPathways.LoginSignup.Auth.EmailPasswordIsSupervisor
import com.example.UserPathways.LoginSignup.Auth.SaltPasswordEmailIsSupervisor
import com.example.utilitys.wrapperClasses.AwsResultWrapper
import com.example.utilitys.AWSHelperFunctions
import com.example.utilitys.objectsToAWSMaps
import com.plcoding.security.hashing.SaltedHash

class SignupLoginDataSource(): signupLoginInterface {
    override suspend fun login(email: String): AwsResultWrapper<SaltPasswordEmailIsSupervisor> {
        return try {
            val keyToGet = AWSHelperFunctions().KeyToGet(email, "signIn")
            val request = AWSHelperFunctions().BuildGetItemRequest(keyToGet)
            val result = AWSHelperFunctions().GetItem(request)!!
            val authSaltPassword = SaltPasswordEmailIsSupervisor(
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

    override suspend fun signup(Authrequest: EmailPasswordIsSupervisor, saltedHash: SaltedHash): Boolean {
        return try{
            val itemValues = objectsToAWSMaps().SignupInfoToItemValues(Authrequest, saltedHash)
            val request = AWSHelperFunctions().buildPutItemRequest(itemValues)
            AWSHelperFunctions().PutObject(request)
            true
        } catch (e: Exception) {
            false
        }
    }
}