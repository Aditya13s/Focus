package com.focus.app.data.database.dao

import androidx.room.*
import com.focus.app.data.database.entity.UsageLog
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageLogDao {

    @Query("SELECT * FROM usage_logs ORDER BY openTime DESC")
    fun getAllLogs(): Flow<List<UsageLog>>

    @Query("SELECT * FROM usage_logs WHERE openTime >= :startTime ORDER BY openTime DESC")
    fun getLogsAfter(startTime: Long): Flow<List<UsageLog>>

    @Query("SELECT COUNT(*) FROM usage_logs WHERE wasBlocked = 1 AND openTime >= :startTime")
    suspend fun getBlockedCount(startTime: Long): Int

    @Query("SELECT COUNT(*) FROM usage_logs WHERE wasResisted = 1 AND openTime >= :startTime")
    suspend fun getResistedCount(startTime: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: UsageLog): Long

    @Query("SELECT * FROM usage_logs WHERE openTime >= :startTime AND openTime <= :endTime")
    suspend fun getLogsBetween(startTime: Long, endTime: Long): List<UsageLog>
}
