@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.papb.projectakhirandroid.domain.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionItem(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("name")
    val name: String,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("owner_id")
    val ownerId: String? = null
)
