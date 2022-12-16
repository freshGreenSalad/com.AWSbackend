package com.example.UserPathways.notification.notificationDataClasses

import com.example.UserPathways.notification.NotificationMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    @SerialName("included_segments")
    val includedSegments: List<String>,
    val contents: NotificationMessage,
    val headings: NotificationMessage,
    @SerialName("app_id")
    val appId: String,
)
