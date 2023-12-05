package com.csr.beneficiary.di


import com.csr.beneficiary.data.api.ApiInterface
import com.csr.beneficiary.data.auth.AuthRepository
import com.csr.beneficiary.data.auth.BackendAuthRepositoryImpl
import com.csr.common.data.AppPrefs
import com.csr.beneficiary.data.database.MyAppDataBase
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
    fun provideUserRepository(
        appDataBase: MyAppDataBase,
        appPrefs: AppPrefs,
        apiInterface: ApiInterface
    ): AuthRepository = BackendAuthRepositoryImpl(
        apiInterface, appDataBase, appPrefs
    )




}