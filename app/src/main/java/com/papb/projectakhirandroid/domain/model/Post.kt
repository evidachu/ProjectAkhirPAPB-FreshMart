@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.papb.projectakhirandroid.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi

@Serializable
data class Post(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("type")
    val type: String, // "resep" atau "tips"

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String,

    @SerialName("image_url")
    val imageUrl: String? = null, // URL string from Supabase

    @SerialName("owner_name")
    val owner: String, // Username display

    @SerialName("owner_id")
    val ownerId: String? = null, // Auth User ID

    // Tambahan field untuk foto profil pemilik postingan
    @SerialName("owner_avatar_url")
    val ownerAvatarUrl: String? = null,

    @SerialName("likes")
    val likes: Int = 0,
    
    @SerialName("comments_count")
    val commentsCount: Int = 0
)
