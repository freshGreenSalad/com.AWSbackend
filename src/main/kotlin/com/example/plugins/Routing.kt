package com.example.plugins

import com.example.*
import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.SupervisorProfileDynamoDBInterface
import com.example.awsServices.dynamoDb.*
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
    tokenConfig: TokenConfig
) {
    routing {
        //puts the initial five profiles into the cloud
        puts3()

        //gets the initial data in the cloud with the key passed in the url
        gets3()

        //used this to upload the test images to the workerImage folder on s3
        putWorkerImage()

        //test send emil
        testSendEmail()

        //aws visualiser route functions for workers
        // putWorkerSignupInfo
        // putWorkerSiteInfo
        // putWorkerSpecialLicence
        // putDatesWorked
        // putWorkerPersonalData
        // putWorkerExperience
        putWorkerSignupInfo(WorkerProfileDataSource, hashingService,  tokenService,  tokenConfig)
        putWorkerSiteInfo(WorkerProfileDataSource)
        putWorkerSpecialLicence(WorkerProfileDataSource)
        putDatesWorked(WorkerProfileDataSource)
        putWorkerPersonalData(WorkerProfileDataSource)
        putWorkerExperience(WorkerProfileDataSource)

        //aws visualiser route functions for workers
        // getWorkerSignupInfo
        // getWorkerSiteInfo
        // getWorkerSpecialLicence
        // getDatesWorked
        // getWorkerPersonalData
        // getWorkerExperience
        getWorkerSignupAuth(WorkerProfileDataSource, hashingService, tokenService,   tokenConfig)
        getWorkerSiteInfo(WorkerProfileDataSource)
        getWorkerSpecialLicence(WorkerProfileDataSource)
        getDatesWorked(WorkerProfileDataSource)
        getWorkerPersonalData(WorkerProfileDataSource)
        getWorkerExperience(WorkerProfileDataSource)

        //aws visualiser route functions for Supervisors
        // putSupervisorSignupInfo
        // putSupervisorSiteInfo
        // putSupervisorPersonalData
        putSupervisorSignupInfo(SupervisorProfileDataSource,  hashingService, tokenService,  tokenConfig)
        putSupervisorSiteInfo(SupervisorProfileDataSource)
        putSupervisorPersonalData(SupervisorProfileDataSource)

        //aws visualiser route functions for Supervisors
        // getSupervisorSignupInfo
        // getSupervisorSiteInfo
        // getSupervisorPersonalData
        getSupervisorSignupInfo(SupervisorProfileDataSource,  hashingService, tokenService,  tokenConfig)
        getSupervisorSiteInfo(SupervisorProfileDataSource)
        getSupervisorPersonalData(SupervisorProfileDataSource)
    }
}
