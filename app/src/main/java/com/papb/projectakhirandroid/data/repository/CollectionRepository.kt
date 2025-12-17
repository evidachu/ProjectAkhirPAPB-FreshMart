@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.papb.projectakhirandroid.data.repository

import android.content.Context
import android.util.Log
import com.papb.projectakhirandroid.domain.model.CollectionItem
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File
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

    // Helper: Upload Image to Storage (Bucket: collections)
    private suspend fun uploadCollectionImage(file: File): String? {
        return try {
            val fileName = "collection_${System.currentTimeMillis()}.jpg"
            val bucket = supabaseClient.storage.from("collections")
            
            // Upload file
            bucket.upload(fileName, file.readBytes())
            
            // Get public URL
            val publicUrl = bucket.publicUrl(fileName)
            Log.d("CollectionRepository", "Upload success: $publicUrl")
            publicUrl
        } catch (e: Exception) {
            Log.e("CollectionRepository", "Upload failed image", e)
            null
        }
    }

    // 2. Add Collection (Create)
    suspend fun addCollection(name: String, imageFile: File?): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id

                // Upload image if exists
                val imageUrl = if (imageFile != null) uploadCollectionImage(imageFile) else null

                val newCollection = CreateCollectionDto(
                    name = name,
                    imageUrl = imageUrl,
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
    suspend fun updateCollection(
        id: Long,
        name: String,
        existingImageUrl: String?,
        newImageFile: File?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Determine the final image URL
                val finalImageUrl = if (newImageFile != null) {
                    uploadCollectionImage(newImageFile)
                } else {
                    existingImageUrl
                }

                val updateDto = UpdateCollectionDto(
                    name = name,
                    imageUrl = finalImageUrl
                )

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
