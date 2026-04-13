package com.focus.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_logs")
data class UsageLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val openTime: Long = System.currentTimeMillis(),
    val reason: String = "",
    val wasBlocked: Boolean = false,
    val wasResisted: Boolean = false
)
