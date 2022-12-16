package com.example.UserPathways.LoginSignup

import com.example.UserPathways.Employee.WorkerProfileDynamoDBInterface
import com.example.UserPathways.LoginSignup.Auth.EmailPassword
import com.example.UserPathways.LoginSignup.Auth.EmailPasswordIsSupervisorPushId
import com.example.UserPathways.LoginSignup.Auth.TokinIsSupervisor
import com.example.UserPathways.LoginSignup.Auth.SaltPasswordEmailIsSupervisor
import com.example.UserPathways.notification.NotificationMessage
import com.example.UserPathways.notification.OneSignalServiceImplemention
import com.plcoding.security.hashing.HashingService
import com.plcoding.security.hashing.SaltedHash
import com.plcoding.security.token.TokenClaim
import com.plcoding.security.token.TokenConfig
import com.plcoding.security.token.TokenService
import com.tamaki.workerapp.userPathways.notification.notificationDataClasses.NotificationToIndividualUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.apache.commons.codec.digest.DigestUtils

fun Route.LoginInfo(
    signup: signupLoginInterface,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("LoginInfo") {
        val request = kotlin.runCatching { call.receiveNullable<EmailPassword>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        println(request.email)
        val profile = signup.login(request.email).data!!
        val isValidPassword = CheckGivenPasswordMatchsStoredPassword(hashingService, request, profile)
        if (!isValidPassword) {
            println("Entered hash: ${DigestUtils.sha256Hex("${profile.salt}${request.password}")}, Hashed PW: ${profile.password}")
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }
        val token = tokenService.generate(
            config = tokenConfig, TokenClaim(
                name = "userId",
                value = request.email
            )
        )
        RespondWithTokenAndSupervisorStatus(token, profile.isSupervisor)
    }
}

fun Route.SignupInfo(
    signup: signupLoginInterface,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    oneSignal: OneSignalServiceImplemention
) {
    post("SignupInfo") {
        val request = kotlin.runCatching { call.receiveNullable<EmailPasswordIsSupervisorPushId>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val wasAcknowledged = signup.signup(request,saltedHash)
        if (!wasAcknowledged) { call.respond(HttpStatusCode.Conflict)
            return@post
        }
        val token = generateTokin(tokenService, tokenConfig, request)
        val testNotification = NotificationToIndividualUser(
            includePlayerIds = listOf(request.email),
            contents = NotificationMessage("test"),
            headings = NotificationMessage("test"),
            appId = "d8f8fee0-faee-4efd-9125-5070286b5c82",
        )
        val successful = oneSignal.sendNotification(testNotification)
        call.respond(
            status = HttpStatusCode.OK,
            message = TokinIsSupervisor(
                token = token,
                isSupervisor = true
            )
        )
    }
}

fun Route.deleteAccount(
    WorkerDataSource: WorkerProfileDynamoDBInterface
) {
    authenticate {
        post("deleteAccount") {
            val authEmail = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
            println("delete this account $authEmail" )
            WorkerDataSource.deleteAccount(authEmail)
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun generateTokin(
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    request: EmailPasswordIsSupervisorPushId
): String {
    val token = tokenService.generate(
        config = tokenConfig, TokenClaim(
            name = "userId",
            value = request.email
        )
    )
    return token
}

private suspend fun PipelineContext<Unit, ApplicationCall>.RespondWithTokenAndSupervisorStatus(
    token: String,
    isSupervisor:Boolean
) {
    call.respond(
        status = HttpStatusCode.OK,
        message = TokinIsSupervisor(
            token = token,
            isSupervisor = isSupervisor
        )
    )
}

private fun CheckGivenPasswordMatchsStoredPassword(
    hashingService: HashingService,
    request: EmailPassword,
    profile: SaltPasswordEmailIsSupervisor
): Boolean {
    val isValidPassword = hashingService.verify(
        value = request.password,
        saltedHash = SaltedHash(
            hash = profile.password,
            salt = profile.salt
        )
    )
    return isValidPassword
}