package com.focus.app.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.focus.app.R
import com.focus.app.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FocusMonitorService : Service() {

    override fun onCreate() {
        super.onCreate()
        startForeground(NotificationHelper.FOCUS_NOTIFICATION_ID, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification() =
        NotificationCompat.Builder(this, NotificationHelper.FOCUS_CHANNEL_ID)
            .setContentTitle("Focus")
            .setContentText("Monitoring app usage in the background")
            .setSmallIcon(R.drawable.ic_focus)
            .setOngoing(true)
            .build()
}
