package com.focus.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.focus.app.data.repository.BlockingRepository
import com.focus.app.ui.overlay.DelayGateActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppBlockerService : AccessibilityService() {

    @Inject
    lateinit var blockingRepository: BlockingRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var lastCheckedPackage = ""
    private var lastCheckTime = 0L

    companion object {
        private const val DEBOUNCE_TIMEOUT_MS = 3_000L
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
            notificationTimeout = 100
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName == applicationContext.packageName) return

        val now = System.currentTimeMillis()
        if (packageName == lastCheckedPackage && (now - lastCheckTime) < DEBOUNCE_TIMEOUT_MS) return
        lastCheckedPackage = packageName
        lastCheckTime = now

        serviceScope.launch {
            if (blockingRepository.isAppBlocked(packageName)) {
                val intent = Intent(applicationContext, DelayGateActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra(DelayGateActivity.EXTRA_PACKAGE_NAME, packageName)
                }
                applicationContext.startActivity(intent)
            }
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
