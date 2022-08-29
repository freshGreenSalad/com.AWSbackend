package com.example


import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import com.example.awsServices.profile.dynamoDbProfileDataSource
import io.ktor.server.application.*
import com.example.plugins.*
import com.plcoding.security.hashing.SHA256HashingService
import com.plcoding.security.token.JwtTokenService
import com.plcoding.security.token.TokenConfig

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val client = DynamoDbClient { region = "ap-southeast-2" }
    val profileDataSource = dynamoDbProfileDataSource(client)
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
        ProfileDataSource = profileDataSource,
        hashingService = hashingService,
        tokenService = tokenService,
        tokenConfig = tokenConfig
    )
    configureSecurity(tokenConfig)
}
