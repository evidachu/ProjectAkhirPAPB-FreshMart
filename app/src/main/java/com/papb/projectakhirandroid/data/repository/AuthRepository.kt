package com.papb.projectakhirandroid.data.repository

import com.papb.projectakhirandroid.data.SupabaseClientProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {

    private val authClient = supabaseClient.auth

    // Indikator apakah proses loading session awal sudah selesai
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    // Menggunakan sessionStatus dari Supabase untuk memantau perubahan status autentikasi secara real-time.
    val authState: Flow<Boolean> = authClient.sessionStatus.map { status ->
        status is SessionStatus.Authenticated
    }

    init {
        // PENTING: Load session dari storage saat aplikasi dimulai
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Mencoba memuat sesi yang tersimpan
                authClient.loadFromStorage()
            } catch (e: Exception) {
                // Ignore errors, user will be logged out
            } finally {
                // Tandai bahwa inisialisasi selesai, entah berhasil atau gagal load
                _isReady.value = true
            }
        }
    }

    /**
     * Melakukan registrasi user baru dengan nama, email dan password.
     */
    suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            authClient.signUpWith(Email) {
                this.email = email
                this.password = password
                // Kirim metadata user (nama) ke Auth Supabase
                data = buildJsonObject {
                    put("full_name", name)
                }
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
     * Melakukan logout.
     */
    suspend fun logout() {
        try {
            authClient.signOut()
        } catch (e: Exception) {
            // Error handling ignored for logout
        }
    }

    /**
     * Memeriksa apakah ada user yang sedang login saat ini.
     */
    fun isUserLoggedIn(): Boolean {
        return authClient.currentSessionOrNull() != null
    }
}
