package com.focus.app.utils

object XpCalculator {

    const val XP_PER_POMODORO = 10
    const val XP_PER_RESISTED = 5
    const val XP_PER_LEVEL = 100

    fun calculateLevel(xp: Int): Int = (xp / XP_PER_LEVEL) + 1

    fun xpToNextLevel(xp: Int): Int {
        val level = calculateLevel(xp)
        return (level * XP_PER_LEVEL) - xp
    }

    fun levelProgress(xp: Int): Float {
        val xpInCurrentLevel = xp % XP_PER_LEVEL
        return xpInCurrentLevel.toFloat() / XP_PER_LEVEL.toFloat()
    }

    fun getLevelTitle(level: Int): String = when {
        level < 5 -> "Novice"
        level < 10 -> "Apprentice"
        level < 20 -> "Journeyman"
        level < 30 -> "Expert"
        level < 50 -> "Master"
        else -> "Grandmaster"
    }
}
