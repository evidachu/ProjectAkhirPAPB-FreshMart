package com.papb.projectakhirandroid.domain.model

import android.net.Uri

data class Post(
    val id: Long = System.currentTimeMillis(), // ID unik berdasarkan timestamp
    val type: String, // "resep" atau "tips"
    val title: String,
    val owner: String,
    val description: String,
    val imageUrl: Uri?, // Menggunakan Uri? untuk gambar dari galeri

    // Tambahan field yang umum di Komunitas (jika ada, sesuaikan)
    val username: String = "Anonim",
    val likes: Int = 0,
    val commentsCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)