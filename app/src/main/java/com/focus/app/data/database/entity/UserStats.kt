package com.focus.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey val id: Int = 1,
    val streak: Int = 0,
    val xp: Int = 0,
    val level: Int = 1,
    val totalFocusMinutes: Int = 0,
    val lastActiveDate: Long = 0,
    val emergencyModeActive: Boolean = false
)
