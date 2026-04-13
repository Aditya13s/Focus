package com.focus.app.data.database.dao

import androidx.room.*
import com.focus.app.data.database.entity.UserStats
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {

    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getUserStats(): Flow<UserStats?>

    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getUserStatsOnce(): UserStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserStats(stats: UserStats)

    @Update
    suspend fun updateUserStats(stats: UserStats)

    @Query("UPDATE user_stats SET xp = xp + :xpAmount WHERE id = 1")
    suspend fun addXp(xpAmount: Int)

    @Query("UPDATE user_stats SET streak = :streak WHERE id = 1")
    suspend fun updateStreak(streak: Int)
}
