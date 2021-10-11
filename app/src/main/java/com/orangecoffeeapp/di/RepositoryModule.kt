package com.orangecoffeeapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.orangecoffeeapp.data.repository.AdmissionRepository
import com.orangecoffeeapp.data.repository.AdmissionRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideAdmissionRepository(db:FirebaseFirestore) = AdmissionRepositoryImp(db) as AdmissionRepository



}