package com.papb.projectakhirandroid.di

import android.content.Context
import com.papb.projectakhirandroid.data.SupabaseClientProvider
import com.papb.projectakhirandroid.data.local.ProductDatabase
import com.papb.projectakhirandroid.data.repository.LocalDataSourceImpl
import com.papb.projectakhirandroid.data.repository.OnBoardingOperationImpl
import com.papb.projectakhirandroid.data.repository.Repository
import com.papb.projectakhirandroid.data.session.SharedPreferencesSessionManager
import com.papb.projectakhirandroid.domain.repository.LocalDataSource
import com.papb.projectakhirandroid.domain.repository.OnBoardingOperation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @OptIn(SupabaseInternal::class)
    @Provides
    @Singleton
    fun provideSupabaseClient(
        @ApplicationContext context: Context
    ): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = SupabaseClientProvider.SUPABASE_URL,
            supabaseKey = SupabaseClientProvider.SUPABASE_ANON_KEY
        ) {
            // Menggunakan engine CIO yang lebih stabil untuk upload file
            httpEngine = CIO.create()

            // Mengatur timeout lebih lama (60 detik)
            httpConfig {
                install(HttpTimeout) {
                    requestTimeoutMillis = 60000
                    connectTimeoutMillis = 60000
                    socketTimeoutMillis = 60000
                }
            }

            install(Auth) {
                // Konfigurasi session persistence agar login tetap tersimpan
                // menggunakan SharedPreferences
                sessionManager = SharedPreferencesSessionManager(context)
                
                // Matikan autoLoadFromStorage agar AuthRepository bisa mengontrol
                // kapan loading selesai (untuk keperluan UI loading spinner)
                autoLoadFromStorage = false
                alwaysAutoRefresh = true
            }
            install(Postgrest)
            install(Storage) // Install Plugin Storage! (Penyebab error sebelumnya)
            install(Realtime)
        }
    }

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
