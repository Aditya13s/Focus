package com.focus.app.data.database.dao

import androidx.room.*
import com.focus.app.data.database.entity.FocusSession
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Query("SELECT * FROM focus_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions WHERE startTime >= :startTime ORDER BY startTime DESC")
    fun getSessionsAfter(startTime: Long): Flow<List<FocusSession>>

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM focus_sessions WHERE completed = 1 AND startTime >= :startTime")
    suspend fun getTotalFocusMinutes(startTime: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSession): Long

    @Update
    suspend fun updateSession(session: FocusSession)

    @Query("SELECT COUNT(*) FROM focus_sessions WHERE completed = 1 AND type = 'WORK' AND startTime >= :startTime")
    suspend fun getCompletedWorkSessionsCount(startTime: Long): Int
}
