package com.focus.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationHelper {

    const val FOCUS_CHANNEL_ID = "focus_channel"
    const val BLOCKING_CHANNEL_ID = "blocking_channel"
    const val STREAK_CHANNEL_ID = "streak_channel"
    const val FOCUS_NOTIFICATION_ID = 1001

    fun createNotificationChannels(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java)

        nm.createNotificationChannels(
            listOf(
                NotificationChannel(
                    FOCUS_CHANNEL_ID,
                    "Focus Sessions",
                    NotificationManager.IMPORTANCE_LOW
                ).apply { description = "Notifications for active focus sessions" },

                NotificationChannel(
                    BLOCKING_CHANNEL_ID,
                    "App Blocking",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "Notifications when apps are blocked" },

                NotificationChannel(
                    STREAK_CHANNEL_ID,
                    "Streak & Achievements",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = "Streak and achievement notifications" }
            )
        )
    }
}
