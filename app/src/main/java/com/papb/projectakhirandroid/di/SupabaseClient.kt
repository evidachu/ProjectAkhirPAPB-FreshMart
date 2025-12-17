package com.papb.projectakhirandroid.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout

object SupabaseClientProvider {

    const val SUPABASE_URL = "https://cleopldufgujyxsoctzv.supabase.co"
    const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNsZW9wbGR1Zmd1anl4c29jdHp2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU2NDQ2NTQsImV4cCI6MjA4MTIyMDY1NH0.m8qd41fpfEnQ7j8_eu6dLdq1GH5S_82Ht84_9BI8af0"

    @OptIn(SupabaseInternal::class)
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        // Menggunakan engine CIO yang lebih stabil untuk upload file
        httpEngine = CIO.create()

        // Mengatur timeout lebih lama (60 detik) agar upload tidak gagal/crash karena koneksi
        httpConfig {
            install(HttpTimeout) {
                requestTimeoutMillis = 60000
                connectTimeoutMillis = 60000
                socketTimeoutMillis = 60000
            }
        }

        install(Auth)
        install(Postgrest)
        install(Storage)
        install(Realtime)
    }
}
