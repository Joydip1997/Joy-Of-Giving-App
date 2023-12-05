package com.csr.beneficiary.di


import android.content.Context
import com.csr.beneficiary.data.Order.BackendPickupOrderRepositoryImpl
import com.csr.beneficiary.data.Order.PickupOrderRepository
import com.csr.beneficiary.data.api.ApiInterface
import com.csr.beneficiary.data.database.MyAppDataBase
import com.csr.beneficiary.data.location_repository.LocationRepository
import com.csr.beneficiary.data.location_repository.LocationRepositoryImpl
import com.csr.beneficiary.data.user.UserRepository
import com.csr.beneficiary.data.user.UserRepositoryImpl
import com.google.android.gms.location.LocationServices
import com.csr.beneficiary.location.DefaultLocationClient
import com.csr.beneficiary.location.LocationClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OtherRepositoryModule {




    @Provides
    @Singleton
    fun provideUserRepository(
        appDataBase: MyAppDataBase
    ): UserRepository = UserRepositoryImpl(appDataBase)

    @Provides
    @Singleton
    fun providePickupOrderRepository(
        apiInterface: ApiInterface
    ): PickupOrderRepository = BackendPickupOrderRepositoryImpl(apiInterface)


    @Provides
    @Singleton
    fun provideLocationRepository(
        apiInterface: ApiInterface
    ): LocationRepository = LocationRepositoryImpl(apiInterface)


    @Provides
    @Singleton
    fun provideFuseLocationProvide(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideLocationClient(@ApplicationContext context: Context): LocationClient =
        DefaultLocationClient(
            context,
            LocationServices.getFusedLocationProviderClient(context)
        )


}