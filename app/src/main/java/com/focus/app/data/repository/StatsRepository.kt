package com.focus.app.data.repository

import com.focus.app.data.database.dao.UsageLogDao
import com.focus.app.data.database.dao.UserStatsDao
import com.focus.app.data.database.entity.UsageLog
import com.focus.app.data.database.entity.UserStats
import com.focus.app.utils.XpCalculator
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val usageLogDao: UsageLogDao,
    private val userStatsDao: UserStatsDao
) {
    fun getAllLogs(): Flow<List<UsageLog>> = usageLogDao.getAllLogs()

    fun getUserStats(): Flow<UserStats?> = userStatsDao.getUserStats()

    suspend fun logAppOpen(log: UsageLog) = usageLogDao.insertLog(log)

    suspend fun getTodayBlockedCount(): Int = usageLogDao.getBlockedCount(getStartOfDay())

    suspend fun getTodayResistedCount(): Int = usageLogDao.getResistedCount(getStartOfDay())

    suspend fun awardXpForResisting() {
        val stats = userStatsDao.getUserStatsOnce() ?: UserStats()
        val newXp = stats.xp + XpCalculator.XP_PER_RESISTED
        val newLevel = XpCalculator.calculateLevel(newXp)
        userStatsDao.updateUserStats(stats.copy(xp = newXp, level = newLevel))
    }

    suspend fun updateUserStats(stats: UserStats) = userStatsDao.updateUserStats(stats)

    suspend fun checkAndUpdateStreak() {
        val stats = userStatsDao.getUserStatsOnce() ?: UserStats()
        val today = getStartOfDay()
        val yesterday = today - 86_400_000L

        val resistedYesterday = usageLogDao.getBlockedCount(yesterday) == 0 ||
                usageLogDao.getResistedCount(yesterday) > 0

        val newStreak = if (stats.lastActiveDate >= yesterday && resistedYesterday) {
            stats.streak + 1
        } else if (stats.lastActiveDate < yesterday) {
            0
        } else {
            stats.streak
        }

        userStatsDao.updateUserStats(stats.copy(streak = newStreak, lastActiveDate = today))
    }

    private fun getStartOfDay(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
