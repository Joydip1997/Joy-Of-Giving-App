package com.csr.auth.auth.di


import com.csr.auth.auth.data.api.ApiInterface
import com.csr.auth.auth.data.auth.AuthRepository
import com.csr.auth.auth.data.auth.BackendAuthRepositoryImpl
import com.csr.auth.auth.data.database.MyAppDataBase
import com.csr.auth.auth.data.repo.UserRepository
import com.csr.auth.auth.data.repo.UserRepositoryImpl
import com.csr.common.data.AppPrefs
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


    @Provides
    @Singleton
    fun provideUserRepository(
        appDataBase: MyAppDataBase
    ): UserRepository = UserRepositoryImpl(
        appDataBase
    )


}