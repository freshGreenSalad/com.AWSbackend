package com.example.awsServices.dynamoDb.LoginSignup

import com.example.Data.RoutingInterfaces.WorkerProfileDynamoDBInterface
import com.example.Data.models.Auth.AuthRequest
import com.example.Data.models.Auth.AuthRequestWithIsSupervisor
import com.example.Data.models.Auth.AuthResponse
import com.example.Data.models.Auth.AuthSaltPasswordEmail
import com.plcoding.security.hashing.HashingService
import com.plcoding.security.hashing.SaltedHash
import com.plcoding.security.token.TokenClaim
import com.plcoding.security.token.TokenConfig
import com.plcoding.security.token.TokenService
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
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: kotlin.run {
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
    tokenConfig: TokenConfig
) {
    post("SignupInfo") {
        val request = kotlin.runCatching { call.receiveNullable<AuthRequestWithIsSupervisor>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val wasAcknowledged = signup.signup(request,saltedHash)
        if (!wasAcknowledged) { call.respond(HttpStatusCode.Conflict)
            return@post
        }
        val token = generateTokin(tokenService, tokenConfig, request)
        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
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
    request: AuthRequestWithIsSupervisor
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
        message = AuthResponse(
            token = token,
            isSupervisor = isSupervisor
        )
    )
}

private fun CheckGivenPasswordMatchsStoredPassword(
    hashingService: HashingService,
    request: AuthRequest,
    profile: AuthSaltPasswordEmail
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