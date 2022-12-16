package com.example.UserPathways.LoginSignup

import com.example.UserPathways.LoginSignup.Auth.EmailPasswordIsSupervisorPushId
import com.example.UserPathways.LoginSignup.Auth.SaltPasswordEmailIsSupervisor
import com.example.utilitys.wrapperClasses.AwsResultWrapper
import com.plcoding.security.hashing.SaltedHash

interface signupLoginInterface {
    suspend fun login(email: String): AwsResultWrapper<SaltPasswordEmailIsSupervisor>
    suspend fun signup(Authrequest: EmailPasswordIsSupervisorPushId, saltedHash: SaltedHash): Boolean
}