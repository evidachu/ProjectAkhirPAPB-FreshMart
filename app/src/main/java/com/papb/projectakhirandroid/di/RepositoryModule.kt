package com.papb.projectakhirandroid.di

import com.papb.projectakhirandroid.data.local.ProductDatabase
import com.papb.projectakhirandroid.data.repository.LocalDataSourceImpl
import com.papb.projectakhirandroid.data.repository.OnBoardingOperationImpl
import com.papb.projectakhirandroid.data.repository.Repository
import com.papb.projectakhirandroid.domain.repository.LocalDataSource
import com.papb.projectakhirandroid.domain.repository.OnBoardingOperation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideOnBoardingOperation(
        impl: OnBoardingOperationImpl
    ): OnBoardingOperation = impl

    @Provides
    @Singleton
    fun provideLocalDataSource(
        productDatabase: ProductDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(productDatabase)
    }

    @Provides
    @Singleton
    fun provideRepository(
        onBoardingOperation: OnBoardingOperation,
        localDataSource: LocalDataSource
    ): Repository {
        return Repository(onBoardingOperation, localDataSource)
    }
}