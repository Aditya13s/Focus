package com.focus.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.focus.app.data.database.dao.AppBlockDao
import com.focus.app.data.database.dao.SessionDao
import com.focus.app.data.database.dao.UsageLogDao
import com.focus.app.data.database.dao.UserStatsDao
import com.focus.app.data.database.entity.BlockedApp
import com.focus.app.data.database.entity.FocusSession
import com.focus.app.data.database.entity.UsageLog
import com.focus.app.data.database.entity.UserStats

@Database(
    entities = [
        BlockedApp::class,
        FocusSession::class,
        UsageLog::class,
        UserStats::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FocusDatabase : RoomDatabase() {
    abstract fun appBlockDao(): AppBlockDao
    abstract fun sessionDao(): SessionDao
    abstract fun usageLogDao(): UsageLogDao
    abstract fun userStatsDao(): UserStatsDao
}
