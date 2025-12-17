package com.papb.projectakhirandroid.presentation.screen.komunitas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papb.projectakhirandroid.data.repository.CommunityRepository
import com.papb.projectakhirandroid.data.repository.UserRepository
import com.papb.projectakhirandroid.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class KomunitasViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // Daftar Postingan yang akan di-display di KomunitasScreen
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    // State loading agar UI tahu kapan harus menampilkan loading spinner
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadPosts()
    }

    /**
     * Mengambil data postingan terbaru dari Supabase
     */
    fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = communityRepository.getPosts()
            _posts.value = result
            _isLoading.value = false
        }
    }

    /**
     * Menyimpan Postingan Baru ke Supabase
     */
    fun createPost(
        title: String,
        description: String,
        type: String,
        imageFile: File?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Ambil nama user yang sedang login dari UserRepository (DataStore)
            val currentUserName = userRepository.getName().first()

            val success = communityRepository.createPost(
                title = title,
                description = description,
                type = type,
                ownerName = currentUserName,
                imageFile = imageFile
            )

            if (success) {
                // Jika berhasil, refresh list postingan
                loadPosts()
            }
            _isLoading.value = false
        }
    }

    /**
     * Memperbarui Postingan yang sudah ada (Edit)
     */
    fun updatePost(
        id: Long,
        title: String,
        description: String,
        existingImageUrl: String?,
        newImageFile: File?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            
            val success = communityRepository.updatePost(
                id = id,
                title = title,
                description = description,
                existingImageUrl = existingImageUrl,
                newImageFile = newImageFile
            )

            if (success) {
                loadPosts()
            }
            _isLoading.value = false
        }
    }

    /**
     * Menghapus Postingan
     */
    fun deletePost(post: Post) {
        viewModelScope.launch {
            val success = communityRepository.deletePost(post.id)
            if (success) {
                // Hapus item dari list lokal agar UI langsung update tanpa loading ulang semua
                _posts.value = _posts.value.filter { it.id != post.id }
            }
        }
    }
}
