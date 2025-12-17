@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.papb.projectakhirandroid.data.repository

import android.content.Context
import com.papb.projectakhirandroid.domain.model.CollectionItem
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

// --- Data Transfer Objects (DTO) ---
@Serializable
data class CreateCollectionDto(
    @SerialName("name") val name: String,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("owner_id") val ownerId: String?
)

@Serializable
data class UpdateCollectionDto(
    @SerialName("name") val name: String,
    @SerialName("image_url") val imageUrl: String?
)

class CollectionRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabaseClient: SupabaseClient
) {

    // 1. Get All Collections (Read)
    suspend fun getCollections(): List<CollectionItem> {
        return withContext(Dispatchers.IO) {
            try {
                // Ambil koleksi milik user yang sedang login saja
                val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id ?: return@withContext emptyList()
                supabaseClient.postgrest.from("collections")
                    .select { filter { eq("owner_id", userId) } }
                    .decodeList<CollectionItem>()
                    .sortedByDescending { it.id }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // 2. Add Collection (Create)
    suspend fun addCollection(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id

                val newCollection = CreateCollectionDto(
                    name = name,
                    imageUrl = null, // No image upload
                    ownerId = userId
                )

                supabaseClient.postgrest.from("collections").insert(newCollection)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
    
    // 3. Update Collection (Update)
    suspend fun updateCollection(id: Long, name: String, existingImageUrl: String?): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val updateDto = UpdateCollectionDto(name = name, imageUrl = existingImageUrl)

                supabaseClient.postgrest.from("collections").update(updateDto) {
                    filter { eq("id", id) }
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    // 4. Delete Collection (Delete)
    suspend fun deleteCollection(id: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                supabaseClient.postgrest.from("collections").delete {
                    filter {
                        eq("id", id)
                    }
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}
