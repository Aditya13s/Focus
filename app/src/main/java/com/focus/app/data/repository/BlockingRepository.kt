package com.focus.app.data.repository

import com.focus.app.data.database.dao.AppBlockDao
import com.focus.app.data.database.entity.BlockedApp
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockingRepository @Inject constructor(
    private val appBlockDao: AppBlockDao
) {
    fun getAllBlockedApps(): Flow<List<BlockedApp>> = appBlockDao.getAllBlockedApps()

    fun getEnabledBlockedApps(): Flow<List<BlockedApp>> = appBlockDao.getEnabledBlockedApps()

    suspend fun addBlockedApp(app: BlockedApp) = appBlockDao.insertBlockedApp(app)

    suspend fun updateBlockedApp(app: BlockedApp) = appBlockDao.updateBlockedApp(app)

    suspend fun removeBlockedApp(app: BlockedApp) = appBlockDao.deleteBlockedApp(app)

    suspend fun getBlockedApp(packageName: String): BlockedApp? =
        appBlockDao.getBlockedApp(packageName)

    suspend fun incrementOpenCount(packageName: String) =
        appBlockDao.incrementOpenCount(packageName)

    suspend fun resetAllOpenCounts() = appBlockDao.resetAllOpenCounts()

    suspend fun isAppBlocked(packageName: String): Boolean {
        val app = appBlockDao.getBlockedApp(packageName) ?: return false
        if (!app.isEnabled) return false

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (currentHour < app.timeWindowStart || currentHour > app.timeWindowEnd) return false

        return true
    }

    suspend fun hasExceededQuota(packageName: String): Boolean {
        val app = appBlockDao.getBlockedApp(packageName) ?: return false
        return app.openCount >= app.dailyQuota
    }
}
