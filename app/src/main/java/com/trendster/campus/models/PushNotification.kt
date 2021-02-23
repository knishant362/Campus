package com.trendster.campus.models

import com.trendster.campus.models.NotificationData

data class PushNotification (
        val data: NotificationData,
        val to: String
    )