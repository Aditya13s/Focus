package com.focus.app.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Repository bindings are provided via @Inject constructor + @Singleton annotations
 * directly on each repository class. No explicit @Provides needed here.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule
