package com.csr.common.di

import android.content.Context
import com.csr.common.data.AppPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {



    @Provides
    @Singleton
    fun provideUserPreference(@ApplicationContext context: Context) : AppPrefs = AppPrefs(context = context)

}