package com.focus.app.data.database.dao

import androidx.room.*
import com.focus.app.data.database.entity.BlockedApp
import kotlinx.coroutines.flow.Flow

@Dao
interface AppBlockDao {

    @Query("SELECT * FROM blocked_apps")
    fun getAllBlockedApps(): Flow<List<BlockedApp>>

    @Query("SELECT * FROM blocked_apps WHERE isEnabled = 1")
    fun getEnabledBlockedApps(): Flow<List<BlockedApp>>

    @Query("SELECT * FROM blocked_apps WHERE packageName = :packageName")
    suspend fun getBlockedApp(packageName: String): BlockedApp?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedApp(app: BlockedApp)

    @Update
    suspend fun updateBlockedApp(app: BlockedApp)

    @Delete
    suspend fun deleteBlockedApp(app: BlockedApp)

    @Query("UPDATE blocked_apps SET openCount = openCount + 1 WHERE packageName = :packageName")
    suspend fun incrementOpenCount(packageName: String)

    @Query("UPDATE blocked_apps SET openCount = 0")
    suspend fun resetAllOpenCounts()

    @Query("SELECT COUNT(*) FROM blocked_apps WHERE isEnabled = 1")
    suspend fun getEnabledBlockedAppsCount(): Int
}
