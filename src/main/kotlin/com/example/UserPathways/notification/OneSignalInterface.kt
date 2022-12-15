package com.example.UserPathways.notification

interface OneSignalInterface {
    suspend fun sendNotification(notification: Notification):Boolean

    companion object{
        const val OnesignalappID = "d8f8fee0-faee-4efd-9125-5070286b5c82"

        const val notifications = "https://onesignal.com/api/v1/notifications"
    }
}