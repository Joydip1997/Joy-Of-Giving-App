package com.csr.donor.di


import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.firebase.storage.FirebaseStorage
import com.csr.donor.data.Order.BackendPickupOrderRepositoryImpl
import com.csr.donor.data.Order.PickupOrderRepository
import com.csr.donor.data.api.ApiInterface
import com.csr.donor.data.database.MyAppDataBase
import com.csr.donor.data.file.FileRepository
import com.csr.donor.data.file.FileRepositoryImpl
import com.csr.donor.data.location_repository.LocationRepository
import com.csr.donor.data.location_repository.LocationRepositoryImpl
import com.csr.donor.data.user.UserRepository
import com.csr.donor.data.user.UserRepositoryImpl
import com.csr.donor.location.DefaultLocationClient
import com.csr.donor.location.LocationClient
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
    fun provideFileRepositoryImpl(firebaseStorage: FirebaseStorage): FileRepository =
        FileRepositoryImpl(firebaseStorage)

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