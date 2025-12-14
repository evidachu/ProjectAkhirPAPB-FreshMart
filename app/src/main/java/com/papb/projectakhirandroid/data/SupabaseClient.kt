package com.papb.projectakhirandroid.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {

    private const val SUPABASE_URL = "https://cleopldufgujyxsoctzv.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNsZW9wbGR1Zmd1anl4c29jdHp2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU2NDQ2NTQsImV4cCI6MjA4MTIyMDY1NH0.m8qd41fpfEnQ7j8_eu6dLdq1GH5S_82Ht84_9BI8af0"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }
}
