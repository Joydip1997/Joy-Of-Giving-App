package com.csr.beneficiary.di


import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseRepositoryModule {


    @Provides
    @Singleton
    fun provideFirestore(
    ) = FirebaseFirestore.getInstance()


}