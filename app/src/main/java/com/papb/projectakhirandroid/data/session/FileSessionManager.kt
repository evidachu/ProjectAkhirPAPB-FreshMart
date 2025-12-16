package com.papb.projectakhirandroid.data.session

import android.content.Context
import io.github.jan.supabase.gotrue.SessionManager
import io.github.jan.supabase.gotrue.user.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileSessionManager(context: Context) : SessionManager {

    private val file = File(context.filesDir, "supabase_session.json")
    
    // Konfigurasi JSON yang lebih toleran terhadap perubahan struktur data
    private val json = Json { 
        ignoreUnknownKeys = true 
        encodeDefaults = true
        isLenient = true
    }

    override suspend fun saveSession(session: UserSession) {
        withContext(Dispatchers.IO) {
            try {
                file.writeText(json.encodeToString(session))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun loadSession(): UserSession? {
        return withContext(Dispatchers.IO) {
            if (file.exists()) {
                try {
                    json.decodeFromString<UserSession>(file.readText())
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            } else {
                null
            }
        }
    }

    override suspend fun deleteSession() {
        withContext(Dispatchers.IO) {
            if (file.exists()) {
                file.delete()
            }
        }
    }
}
