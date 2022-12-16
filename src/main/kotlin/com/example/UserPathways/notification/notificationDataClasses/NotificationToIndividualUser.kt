package com.tamaki.workerapp.userPathways.notification.notificationDataClasses

import com.example.UserPathways.notification.NotificationMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationToIndividualUser(
    @SerialName("include_player_ids")
    val includePlayerIds: List<String>,
    val contents: NotificationMessage,
    val headings: NotificationMessage,
    @SerialName("app_id")
    val appId: String,
)
