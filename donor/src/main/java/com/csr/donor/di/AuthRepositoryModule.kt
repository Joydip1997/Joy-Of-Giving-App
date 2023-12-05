package com.csr.donor.di


import com.csr.donor.data.api.ApiInterface
import com.csr.donor.data.auth.AuthRepository
import com.csr.donor.data.auth.BackendAuthRepositoryImpl
import com.csr.common.data.AppPrefs
import com.csr.donor.data.database.MyAppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthRepositoryModule {


    @Provides
    @Singleton
    fun provideAuthRepository(
        appDataBase: MyAppDataBase,
        appPrefs: AppPrefs,
        apiInterface: ApiInterface
    ): AuthRepository = BackendAuthRepositoryImpl(
        apiInterface, appDataBase, appPrefs
    )




}