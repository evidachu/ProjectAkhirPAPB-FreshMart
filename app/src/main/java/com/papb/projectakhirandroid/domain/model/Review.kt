@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.papb.projectakhirandroid.domain.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("product_id")
    val productId: Int,

    @SerialName("user_id")
    val userId: String,

    @SerialName("username")
    val username: String,

    @SerialName("user_profile_pic_url")
    val userProfilePicUrl: String? = null, // URL for the user's profile picture

    @SerialName("rating")
    val rating: Int,

    @SerialName("review_text")
    val reviewText: String,

    @SerialName("review_image_url")
    val reviewImageUrl: String? = null // URL for the uploaded review image
)
