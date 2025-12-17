package com.papb.projectakhirandroid.data.repository

import android.content.Context
import android.net.Uri
import com.papb.projectakhirandroid.domain.model.Review
import com.papb.projectakhirandroid.utils.ImageUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // 2. Add a new Review (Create)
    suspend fun addReview(
        productId: Int,
        rating: Int,
        reviewText: String,
        imageUri: Uri?,
        username: String,
        userProfilePicUrl: String?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id ?: return@withContext false
                var reviewImageUrl: String? = null

                if (imageUri != null) {
                    reviewImageUrl = uploadReviewImage(imageUri, userId, productId)
                }

                val newReview = Review(
                    productId = productId,
                    userId = userId,
                    username = username,
                    userProfilePicUrl = userProfilePicUrl,
                    rating = rating,
                    reviewText = reviewText,
                    reviewImageUrl = reviewImageUrl
                )

                supabaseClient.postgrest.from("reviews").insert(newReview)
                true
            } catch (e: Throwable) {
                e.printStackTrace()
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
        imageUri: Uri?,
        existingImageUrl: String?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id ?: return@withContext false
                var finalImageUrl = existingImageUrl

                if (imageUri != null) {
                    finalImageUrl = uploadReviewImage(imageUri, userId, productId)
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
                e.printStackTrace()
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
                e.printStackTrace()
                false
            }
        }
    }

    // Helper: Upload Image to Storage bucket 'reviews'
    private suspend fun uploadReviewImage(uri: Uri, userId: String, productId: Int): String? {
        return try {
            // Gunakan ImageUtils untuk kompresi dan konversi ke ByteArray
            val byteArray = ImageUtils.uriToByteArray(context, uri) ?: return null

            // Nama file unik: reviews/{productId}/{userId}_{timestamp}.jpg
            val fileName = "reviews/$productId/${userId}_${System.currentTimeMillis()}.jpg"
            val bucket = supabaseClient.storage.from("reviews") // Pastikan bucket 'reviews' ada

            bucket.upload(fileName, byteArray, upsert = true)
            bucket.publicUrl(fileName)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }
}
