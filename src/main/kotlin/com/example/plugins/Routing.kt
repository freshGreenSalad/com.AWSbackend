package com.example.plugins

import com.example.*
import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.SupervisorProfileDynamoDBInterface
import com.example.awsServices.dynamoDb.*
import com.example.awsServices.dynamoDb.LoginSignup.*
import com.example.awsServices.dynamoDb.employer.*
import com.example.awsServices.ses.testSendEmail
import com.plcoding.security.hashing.HashingService
import com.plcoding.security.token.TokenConfig
import com.plcoding.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    WorkerProfileDataSource: WorkerProfileDynamoDBInterface,
    SupervisorProfileDataSource: SupervisorProfileDynamoDBInterface,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    SignupDataSource: signupLoginInterface
) {
    routing {
        //test presign
        testPresign()
        //puts the initial five profiles into the cloud
        puts3()

        //gets the initial data in the cloud with the key passed in the url
        gets3()

        //used this to upload the test images to the workerImage folder on s3
        putWorkerImage()

        //test send emil
        testSendEmail()

        workerSiteInfo(WorkerProfileDataSource)
        WorkerSpecialLicence(WorkerProfileDataSource)
        DatesWorked(WorkerProfileDataSource)
        WorkerPersonalData(WorkerProfileDataSource)
        WorkerExperience(WorkerProfileDataSource)
        WorkerDriversLicence(WorkerProfileDataSource)

        SupervisorSiteInfo(SupervisorProfileDataSource)
        SupervisorPersonalData(SupervisorProfileDataSource)
        getWorkerS(WorkerProfileDataSource)

        LoginInfo(SignupDataSource, hashingService, tokenService,   tokenConfig)
        SignupInfo(SignupDataSource,  hashingService, tokenService,  tokenConfig)
        deleteAccount(WorkerProfileDataSource)
    }
}