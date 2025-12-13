package com.papb.projectakhirandroid.domain.model

import android.net.Uri

data class Review(
    val id: Int,
    val username: String,
    val userProfilePic: Int, // Using a drawable resource for the profile picture
    val rating: Int,
    val reviewText: String,
    val reviewImage: String? = null
)
