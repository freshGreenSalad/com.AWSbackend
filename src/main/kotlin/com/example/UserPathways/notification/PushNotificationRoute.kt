package com.example.UserPathways.notification


import com.tamaki.workerapp.userPathways.notification.notificationDataClasses.NotificationToIndividualUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.PushNotification(service: OneSignalServiceImplemention){
    get("sendNotification"){
        val testNotification = NotificationToIndividualUser(
            includePlayerIds = listOf("Subscribed Users"),//need to pass user email here
            contents = NotificationMessage("you are signed up"),
            headings = NotificationMessage("test"),
            appId = "d8f8fee0-faee-4efd-9125-5070286b5c82",
        )
        val successful = service.sendNotification(testNotification)
        if (successful){
            call.respond(HttpStatusCode.OK)
        }else{
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}

