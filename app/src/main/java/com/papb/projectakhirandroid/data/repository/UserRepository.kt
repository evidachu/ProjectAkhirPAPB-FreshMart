package com.papb.projectakhirandroid.data.repository

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.papb.projectakhirandroid.data.dataStore
import com.papb.projectakhirandroid.domain.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    // --- Supabase Cloud Functions ---

    // 1. Dapatkan ID User yang sedang login
    fun getCurrentUserId(): String? {
        // Menggunakan properti 'auth' (sebelumnya 'gotrue')
        return supabaseClient.auth.currentSessionOrNull()?.user?.id
    }

    // 2. Simpan Profil ke Database Supabase
    suspend fun upsertUserProfileToSupabase(name: String, email: String, avatarUrl: String?) {
        val userId = getCurrentUserId()
        if (userId != null) {
            val userProfile = UserProfile(
                id = userId,
                fullName = name,
                email = email,
                avatarUrl = avatarUrl
            )
            try {
                // Perbaikan: onConflict adalah parameter fungsi, bukan di dalam lambda
                supabaseClient.postgrest.from("profiles").upsert(
                    value = userProfile,
                    onConflict = "id"
                )
            } catch (e: Exception) {
                e.printStackTrace()
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
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
