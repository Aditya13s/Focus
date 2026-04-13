package com.focus.app.data.repository

import com.focus.app.data.database.dao.SessionDao
import com.focus.app.data.database.dao.UserStatsDao
import com.focus.app.data.database.entity.FocusSession
import com.focus.app.data.database.entity.UserStats
import com.focus.app.utils.XpCalculator
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusRepository @Inject constructor(
    private val sessionDao: SessionDao,
    private val userStatsDao: UserStatsDao
) {
    fun getAllSessions(): Flow<List<FocusSession>> = sessionDao.getAllSessions()

    fun getUserStats(): Flow<UserStats?> = userStatsDao.getUserStats()

    suspend fun startSession(session: FocusSession): Long = sessionDao.insertSession(session)

    suspend fun completeSession(session: FocusSession) {
        sessionDao.updateSession(
            session.copy(completed = true, endTime = System.currentTimeMillis())
        )

        val stats = userStatsDao.getUserStatsOnce() ?: UserStats()
        val newXp = stats.xp + XpCalculator.XP_PER_POMODORO
        val newLevel = XpCalculator.calculateLevel(newXp)
        userStatsDao.updateUserStats(
            stats.copy(
                totalFocusMinutes = stats.totalFocusMinutes + session.durationMinutes,
                xp = newXp,
                level = newLevel
            )
        )
    }

    suspend fun getTodayFocusMinutes(): Int {
        return sessionDao.getTotalFocusMinutes(getStartOfDay())
    }

    suspend fun ensureUserStatsExist() {
        if (userStatsDao.getUserStatsOnce() == null) {
            userStatsDao.insertUserStats(UserStats())
        }
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
