@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.papb.projectakhirandroid.domain.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    // ID should match the user's ID from Supabase Auth (usually a String/UUID)
    @SerialName("id")
    val id: String,

    @SerialName("full_name")
    var fullName: String,

    @SerialName("avatar_url")
    var avatarUrl: String? = null,

    // Email is usually handled by the auth schema, but can be here too
    // For now, let's assume it's part of the profile table for simplicity.
    @SerialName("email")
    var email: String
)
