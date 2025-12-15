package com.papb.projectakhirandroid.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.papb.projectakhirandroid.data.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val PROFILE_IMAGE_URI = stringPreferencesKey("profile_image_uri")
    }

    fun getName(): Flow<String> = context.dataStore.data.map {
        it[PreferencesKeys.NAME] ?: "John Doe" // Default value
    }

    suspend fun saveName(name: String) {
        context.dataStore.edit { it[PreferencesKeys.NAME] = name }
    }

    fun getEmail(): Flow<String> = context.dataStore.data.map {
        it[PreferencesKeys.EMAIL] ?: "johndoe@example.com" // Default value
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { it[PreferencesKeys.EMAIL] = email }
    }

    fun getProfileImageUri(): Flow<String?> = context.dataStore.data.map {
        it[PreferencesKeys.PROFILE_IMAGE_URI]
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

    suspend fun logout() {
        context.dataStore.edit {
            it.clear()
        }
    }
}
