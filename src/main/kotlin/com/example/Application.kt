package com.example

import com.example.UserPathways.Employee.WorkerProfileDynamoDBDataSource
import com.example.UserPathways.LoginSignup.SignupLoginDataSource
import com.example.UserPathways.Search.SearchDataSource
import com.example.UserPathways.employer.SupervisorProfileDynamoDBDataSource
import com.example.UserPathways.hire.HireWorkerDataSource
import com.example.UserPathways.notification.OneSignalServiceImplemention
import io.ktor.server.application.*
import com.example.plugins.*
import com.plcoding.security.hashing.SHA256HashingService
import com.plcoding.security.token.JwtTokenService
import com.plcoding.security.token.TokenConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val client = HttpClient(CIO){}
    val apikey = environment.config.property("oneSignal.api_key").getString()
    val oneSignal = OneSignalServiceImplemention(client, apikey )
    val searchDataSource = SearchDataSource()
    val hireWorkerDataSourceInterface = HireWorkerDataSource()
    val supervisorProfileDataSource = SupervisorProfileDynamoDBDataSource()
    val workerProfileDynamoDBData = WorkerProfileDynamoDBDataSource()
    val signupDataSource = SignupLoginDataSource()

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureSerialization()
    configureRouting(
        WorkerProfileDataSource = workerProfileDynamoDBData,
        SupervisorProfileDataSource = supervisorProfileDataSource,
        hashingService = hashingService,
        tokenService = tokenService,
        tokenConfig = tokenConfig,
        SignupDataSource = signupDataSource,
        hireWorkerDataSource = hireWorkerDataSourceInterface,
        searchWorkerDataSource = searchDataSource,
        oneSignalInterface = oneSignal
    )
    configureSecurity(tokenConfig)
}
