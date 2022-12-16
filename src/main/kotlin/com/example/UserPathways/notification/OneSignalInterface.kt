package com.example.UserPathways.notification

import com.tamaki.workerapp.userPathways.notification.notificationDataClasses.NotificationToIndividualUser

interface OneSignalInterface {
    suspend fun sendNotification(notification: NotificationToIndividualUser):Boolean

    companion object{
        const val OnesignalappID = "d8f8fee0-faee-4efd-9125-5070286b5c82"

        const val notifications = "https://onesignal.com/api/v1/notifications"
    }
}