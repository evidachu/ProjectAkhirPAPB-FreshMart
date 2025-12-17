package com.papb.projectakhirandroid.data.repository

import android.content.Context
import android.util.Log
import com.papb.projectakhirandroid.domain.model.Review
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabaseClient: SupabaseClient
) {

    // 1. Get Reviews for a Product (Read)
    suspend fun getReviews(productId: Int): List<Review> {
        return withContext(Dispatchers.IO) {
            try {
                supabaseClient.postgrest.from("reviews")
                    .select { filter { eq("product_id", productId) } }
                    .decodeList<Review>()
            } catch (e: Exception) {
                Log.e("ReviewRepository", "Error getting reviews", e)
                emptyList()
            }
        }
    }

    // Helper: Upload Image to Storage
    private suspend fun uploadReviewImage(file: File): String? {
        return try {
            val fileName = "review_${System.currentTimeMillis()}.jpg"
            val bucket = supabaseClient.storage.from("reviews")
            
            // Upload file
            bucket.upload(fileName, file.readBytes())
            
            // Get public URL
            val publicUrl = bucket.publicUrl(fileName)
            Log.d("ReviewRepository", "Upload success: $publicUrl")
            publicUrl
        } catch (e: Exception) {
            Log.e("ReviewRepository", "Upload failed image", e)
            null
        }
    }

    // 2. Add a new Review (Create)
    suspend fun addReview(
        productId: Int,
        rating: Int,
        reviewText: String,
        username: String,
        userProfilePicUrl: String?,
        imageFile: File?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id ?: return@withContext false

                // Upload image if exists
                val imageUrl = if (imageFile != null) uploadReviewImage(imageFile) else null

                val newReview = Review(
                    productId = productId,
                    userId = userId,
                    username = username,
                    userProfilePicUrl = userProfilePicUrl,
                    rating = rating,
                    reviewText = reviewText,
                    reviewImageUrl = imageUrl
                )

                supabaseClient.postgrest.from("reviews").insert(newReview)
                true
            } catch (e: Throwable) {
                Log.e("ReviewRepository", "Error adding review", e)
                false
            }
        }
    }

    // 3. Update Review
    suspend fun updateReview(
        reviewId: Long,
        productId: Int,
        rating: Int,
        reviewText: String,
        existingImageUrl: String?,
        newImageFile: File?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Determine the final image URL
                val finalImageUrl = if (newImageFile != null) {
                    uploadReviewImage(newImageFile)
                } else {
                    existingImageUrl
                }

                val updateData = mapOf(
                    "rating" to rating,
                    "review_text" to reviewText,
                    "review_image_url" to finalImageUrl
                )

                supabaseClient.postgrest.from("reviews").update(updateData) {
                    filter { eq("id", reviewId) }
                }
                true
            } catch (e: Throwable) {
                Log.e("ReviewRepository", "Error updating review", e)
                false
            }
        }
    }

    // 4. Delete Review
    suspend fun deleteReview(reviewId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                supabaseClient.postgrest.from("reviews").delete {
                    filter { eq("id", reviewId) }
                }
                true
            } catch (e: Throwable) {
                Log.e("ReviewRepository", "Error deleting review", e)
                false
            }
        }
    }
}
