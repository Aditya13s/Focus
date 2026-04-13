package com.focus.app.di

import android.content.Context
import androidx.room.Room
import com.focus.app.data.database.FocusDatabase
import com.focus.app.data.database.dao.AppBlockDao
import com.focus.app.data.database.dao.SessionDao
import com.focus.app.data.database.dao.UsageLogDao
import com.focus.app.data.database.dao.UserStatsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFocusDatabase(@ApplicationContext context: Context): FocusDatabase {
        return Room.databaseBuilder(
            context,
            FocusDatabase::class.java,
            "focus_database"
        ).build()
    }

    @Provides
    fun provideAppBlockDao(db: FocusDatabase): AppBlockDao = db.appBlockDao()

    @Provides
    fun provideSessionDao(db: FocusDatabase): SessionDao = db.sessionDao()

    @Provides
    fun provideUsageLogDao(db: FocusDatabase): UsageLogDao = db.usageLogDao()

    @Provides
    fun provideUserStatsDao(db: FocusDatabase): UserStatsDao = db.userStatsDao()
}
