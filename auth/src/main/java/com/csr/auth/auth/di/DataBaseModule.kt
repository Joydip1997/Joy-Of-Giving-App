package com.csr.auth.auth.di

import android.content.Context
import androidx.room.Room
import com.csr.auth.auth.data.database.MyAppDataBase
import com.csr.common.Common.DB_NAME
import com.csr.common.data.AppPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context: Context) : MyAppDataBase = Room.databaseBuilder(
        context.applicationContext,
        MyAppDataBase::class.java, DB_NAME
    ).fallbackToDestructiveMigration().build()



}