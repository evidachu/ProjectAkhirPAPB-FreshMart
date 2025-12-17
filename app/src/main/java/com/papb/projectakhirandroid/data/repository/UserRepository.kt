package com.papb.projectakhirandroid.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.papb.projectakhirandroid.data.dataStore
import com.papb.projectakhirandroid.domain.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabaseClient: SupabaseClient
) {

    private object PreferencesKeys {
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val PROFILE_IMAGE_URI = stringPreferencesKey("profile_image_uri")
    }

    // --- DataStore Local Getters ---
    fun getName(): Flow<String> = context.dataStore.data.map {
        it[PreferencesKeys.NAME] ?: "John Doe"
    }

    fun getEmail(): Flow<String> = context.dataStore.data.map {
        it[PreferencesKeys.EMAIL] ?: "johndoe@example.com"
    }

    fun getProfileImageUri(): Flow<String?> = context.dataStore.data.map {
        it[PreferencesKeys.PROFILE_IMAGE_URI]
    }

    // --- DataStore Local Setters ---
    suspend fun saveName(name: String) {
        context.dataStore.edit { it[PreferencesKeys.NAME] = name }
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { it[PreferencesKeys.EMAIL] = email }
    }

    suspend fun saveProfileImageUri(uri: String?) {
        context.dataStore.edit { preferences ->
            if (uri != null) {
                preferences[PreferencesKeys.PROFILE_IMAGE_URI] = uri
            } else {
                preferences.remove(PreferencesKeys.PROFILE_IMAGE_URI)
            }
        }
    }

    // --- Clear Data Local (PENTING untuk Logout) ---
    suspend fun clearLocalData() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.NAME)
            preferences.remove(PreferencesKeys.EMAIL)
            preferences.remove(PreferencesKeys.PROFILE_IMAGE_URI)
        }
    }

    // --- Supabase Cloud Functions ---

    // 1. Dapatkan ID User yang sedang login
    fun getCurrentUserId(): String? {
        return supabaseClient.auth.currentSessionOrNull()?.user?.id
    }

    // Helper: Upload Profile Image to Storage (Bucket: avatars)
    suspend fun uploadProfileImage(file: File): String? {
        return withContext(Dispatchers.IO) {
            try {
                val userId = getCurrentUserId() ?: return@withContext null
                // Gunakan nama file yang unik atau overwrite file user (misal: "avatar_userid.jpg")
                // Agar hemat storage, kita overwrite saja avatar lama user tersebut
                val fileName = "avatar_$userId.jpg"
                // Menggunakan bucket 'avatars' sesuai request
                val bucket = supabaseClient.storage.from("avatars")

                // Upload file (upsert = true agar menimpa file lama jika ada)
                bucket.upload(fileName, file.readBytes(), upsert = true)

                // Get public URL
                // Kita tambahkan timestamp dummy di URL agar Coil mereload gambar (cache busting)
                val publicUrl = bucket.publicUrl(fileName)
                val publicUrlWithTimestamp = "$publicUrl?t=${System.currentTimeMillis()}"
                
                Log.d("UserRepository", "Upload success: $publicUrlWithTimestamp")
                publicUrlWithTimestamp
            } catch (e: Exception) {
                Log.e("UserRepository", "Upload failed image", e)
                null
            }
        }
    }

    // 2. Simpan Profil ke Database Supabase
    suspend fun upsertUserProfileToSupabase(name: String, email: String, avatarUrl: String?) {
        val userId = getCurrentUserId()
        if (userId != null) {
            
            // Optimistic Update: Simpan ke Lokal dulu agar UI langsung berubah
            saveName(name)
            saveEmail(email)
            saveProfileImageUri(avatarUrl)
            
            val userProfile = UserProfile(
                id = userId,
                fullName = name,
                email = email,
                avatarUrl = avatarUrl
            )
            try {
                // Simpan ke Supabase
                supabaseClient.postgrest.from("profiles").upsert(
                    value = userProfile,
                    onConflict = "id"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("UserRepository", "Failed to upsert profile to Supabase", e)
                // Note: Data lokal sudah terupdate, jadi UI aman. 
                // Namun data cloud mungkin tidak sinkron jika error terus berlanjut.
            }
        }
    }

    // 3. Ambil Profil dari Supabase
    suspend fun fetchUserProfileFromSupabase() {
        val userId = getCurrentUserId() ?: return
        try {
            val userProfile = supabaseClient.postgrest.from("profiles")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }.decodeSingleOrNull<UserProfile>()

            if (userProfile != null) {
                saveName(userProfile.fullName)
                saveEmail(userProfile.email)
                saveProfileImageUri(userProfile.avatarUrl)
            } else {
                // Jika user baru dan belum punya profil di database, pastikan lokal bersih
                // (Optional: Bisa set default value jika perlu)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
