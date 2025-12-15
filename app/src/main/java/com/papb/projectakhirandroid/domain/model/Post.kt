package com.papb.projectakhirandroid.domain.model

import android.net.Uri

data class Post(
    // 🚨 PERBAIKAN: Set id default menjadi 0L. ID unik akan ditetapkan di ViewModel.
    // Menghapus System.currentTimeMillis() di sini.
    val id: Long = 0L,

    val type: String, // "resep" atau "tips"
    val title: String,
    val owner: String,
    val description: String,
    val imageUrl: Uri?, // Menggunakan Uri? untuk gambar dari galeri

    // Tambahan field yang umum di Komunitas (dibiarkan default 0 atau timestamp)
    val username: String = "Anonim",
    val likes: Int = 0,
    val commentsCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis() // Timestamp aman jika ini adalah waktu posting dibuat
)