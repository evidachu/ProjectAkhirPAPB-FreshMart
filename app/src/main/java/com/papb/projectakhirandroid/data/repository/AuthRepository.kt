package com.papb.projectakhirandroid.data.repository

import com.papb.projectakhirandroid.data.SupabaseClientProvider
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {

    private val authClient = SupabaseClientProvider.client.auth

    /**
     * Melakukan registrasi user baru dengan email dan password.
     */
    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            authClient.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Melakukan login dengan email dan password.
     */
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            authClient.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Melakukan logout untuk user yang sedang aktif.
     */
    suspend fun signOut() {
        authClient.signOut()
    }

    /**
     * Memeriksa apakah ada user yang sedang login saat ini.
     */
    fun isUserLoggedIn(): Boolean {
        return authClient.currentSessionOrNull() != null
    }
}