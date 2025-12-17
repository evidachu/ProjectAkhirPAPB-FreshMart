@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.papb.projectakhirandroid.data.repository

import android.content.Context
import android.util.Log
import com.papb.projectakhirandroid.domain.model.Post
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
data class CreatePostDto(
    @SerialName("type") val type: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("owner_name") val ownerName: String,
    @SerialName("owner_id") val ownerId: String?
)

@Serializable
data class UpdatePostDto(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("image_url") val imageUrl: String?
)

class CommunityRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabaseClient: SupabaseClient
) {

    // 1. Get All Posts (Read)
    suspend fun getPosts(): List<Post> {
        return withContext(Dispatchers.IO) {
            try {
                supabaseClient.postgrest.from("posts")
                    .select()
                    .decodeList<Post>()
                    .sortedByDescending { it.id }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // Helper: Upload Image to Storage (Bucket: community)
    private suspend fun uploadPostImage(file: File): String? {
        return try {
            val fileName = "post_${System.currentTimeMillis()}.jpg"
            // Menggunakan bucket 'community' sesuai request
            val bucket = supabaseClient.storage.from("community")
            
            // Upload file
            bucket.upload(fileName, file.readBytes())
            
            // Get public URL
            val publicUrl = bucket.publicUrl(fileName)
            Log.d("CommunityRepository", "Upload success: $publicUrl")
            publicUrl
        } catch (e: Exception) {
            Log.e("CommunityRepository", "Upload failed image", e)
            null
        }
    }

    // 2. Create Post (Create)
    suspend fun createPost(
        title: String,
        description: String,
        type: String,
        ownerName: String,
        imageFile: File?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id

                // Upload image if exists
                val imageUrl = if (imageFile != null) uploadPostImage(imageFile) else null

                val newPost = CreatePostDto(
                    type = type,
                    title = title,
                    description = description,
                    imageUrl = imageUrl,
                    ownerName = ownerName,
                    ownerId = userId
                )

                supabaseClient.postgrest.from("posts").insert(newPost)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    // 3. Update Post (Update)
    suspend fun updatePost(
        id: Long,
        title: String,
        description: String,
        existingImageUrl: String?,
        newImageFile: File?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Determine the final image URL
                val finalImageUrl = if (newImageFile != null) {
                    uploadPostImage(newImageFile)
                } else {
                    existingImageUrl
                }

                val updateData = UpdatePostDto(
                    title = title,
                    description = description,
                    imageUrl = finalImageUrl
                )

                supabaseClient.postgrest.from("posts").update(updateData) {
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

    // 4. Delete Post (Delete)
    suspend fun deletePost(postId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                supabaseClient.postgrest.from("posts").delete {
                    filter {
                        eq("id", postId)
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
