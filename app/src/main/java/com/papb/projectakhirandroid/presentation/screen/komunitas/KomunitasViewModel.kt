package com.papb.projectakhirandroid.presentation.screen.komunitas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papb.projectakhirandroid.domain.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.net.Uri // Diperlukan karena Post.imageUrl bertipe Uri?

class KomunitasViewModel(/* ... dependencies ... */) : ViewModel() {

    // Daftar Postingan yang akan di-display di KomunitasScreen
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    init {
        // Memuat data awal/dummy
        _posts.value = initialDummyPosts()
    }

    /**
     * Menyimpan atau memperbarui Postingan.
     * Jika ID sudah ada (ID != 0L), maka EDIT. Jika ID == 0L, maka ADD/CREATE.
     */
    fun savePost(post: Post) {
        viewModelScope.launch {
            val currentPosts = _posts.value.toMutableList()

            if (post.id != 0L) { // Cek menggunakan Long
                // EDIT: Cari berdasarkan ID dan Ganti
                val existingPostIndex = currentPosts.indexOfFirst { it.id == post.id }
                if (existingPostIndex >= 0) {
                    currentPosts[existingPostIndex] = post
                }
            } else {
                // ADD/CREATE: Beri ID baru (tipe Long)
                val newId = (currentPosts.maxOfOrNull { it.id } ?: 0L) + 1L // ID baru bertipe Long
                currentPosts.add(post.copy(id = newId))
            }
            _posts.value = currentPosts.toList()
            // TODO: Panggil repository untuk menyimpan ke database di skenario nyata
        }
    }

    /**
     * Menghapus Postingan dari daftar.
     */
    fun deletePost(post: Post) {
        viewModelScope.launch {
            _posts.value = _posts.value.filter { it.id != post.id }
            // TODO: Panggil repository untuk menghapus dari database
        }
    }
}

// Data dummy untuk inisialisasi awal
private fun initialDummyPosts(): List<Post> {
    return listOf(
        Post(
            id = 1L, // ID bertipe Long
            type = "resep",
            title = "Resep Ayam Hijau Sehat",
            description = "Resep ayam tinggi protein dan rendah karbohidrat yang mudah dibuat di rumah.",
            imageUrl = null,
            owner = "Chef_Budi"
        ),
        Post(
            id = 2L, // ID bertipe Long
            type = "tips",
            title = "Tips Hemat Belanja Mingguan",
            description = "Cara menyusun menu dan belanja untuk menghemat 30% pengeluaran dapur.",
            imageUrl = null,
            owner = "User_Dina"
        )
    )
}