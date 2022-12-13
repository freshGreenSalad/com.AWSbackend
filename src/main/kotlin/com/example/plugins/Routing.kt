package com.example.plugins

import com.example.*
import com.example.UserPathways.Employee.WorkerProfileDynamoDBInterface
import com.example.UserPathways.LoginSignup.LoginInfo
import com.example.UserPathways.LoginSignup.SignupInfo
import com.example.UserPathways.LoginSignup.deleteAccount
import com.example.UserPathways.LoginSignup.signupLoginInterface
import com.example.UserPathways.employer.SupervisorProfileDynamoDBInterface
import com.example.awsServices.UserPathways.*
import com.example.UserPathways.Search.SearchWorkerInterface
import com.example.UserPathways.Search.SearchWorkers
import com.example.UserPathways.employer.SupervisorPersonalData
import com.example.UserPathways.employer.SupervisorSiteInfo
import com.example.UserPathways.employer.getWorkerS
import com.example.UserPathways.hire.HireWorker
import com.example.UserPathways.hire.interfaces.HireWorkerDataSourceInterface
import com.example.UserPathways.ses.testSendEmail
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
    SignupDataSource: signupLoginInterface,
    hireWorkerDataSource: HireWorkerDataSourceInterface,
    searchWorkerDataSource: SearchWorkerInterface
) {
    routing {

        presignS3StorageRequest()
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
        HireWorker(hireWorkerDataSource)

        SearchWorkers(searchWorkerDataSource)
    }
}