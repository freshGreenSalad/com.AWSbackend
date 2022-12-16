package com.example.UserPathways.notification

import com.example.UserPathways.notification.notificationDataClasses.Notification
import com.tamaki.workerapp.userPathways.notification.notificationDataClasses.NotificationToIndividualUser
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OneSignalServiceImplemention(
    private val client: HttpClient,
    private val apiKey: String
):OneSignalInterface {
    override suspend fun sendNotification(notification: NotificationToIndividualUser): Boolean {
        return try {
            val response = client.post(OneSignalInterface.notifications) {
                contentType(ContentType.Application.Json)
                header("Authorization", "Basic $apiKey")
                setBody(Json.encodeToString<NotificationToIndividualUser>(notification))
            }.status
            println(response)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
