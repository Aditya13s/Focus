package com.focus.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_apps")
data class BlockedApp(
    @PrimaryKey val packageName: String,
    val appName: String,
    val isEnabled: Boolean = true,
    val dailyQuota: Int = 5,
    val openCount: Int = 0,
    val timeWindowStart: Int = 0,
    val timeWindowEnd: Int = 23,
    val lastResetDate: Long = System.currentTimeMillis()
)
