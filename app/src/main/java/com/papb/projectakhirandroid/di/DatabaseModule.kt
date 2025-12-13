package com.papb.projectakhirandroid.di

import android.content.Context
import androidx.room.Room
import com.papb.projectakhirandroid.data.local.ProductDatabase
import com.papb.projectakhirandroid.data.repository.LocalDataSourceImpl
import com.papb.projectakhirandroid.domain.repository.LocalDataSource
import com.papb.projectakhirandroid.utils.Constants.PRODUCT_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            PRODUCT_DATABASE
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        database: ProductDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(database)
    }

}