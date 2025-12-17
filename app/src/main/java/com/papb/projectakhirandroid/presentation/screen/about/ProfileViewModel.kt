package com.papb.projectakhirandroid.presentation.screen.about

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papb.projectakhirandroid.data.repository.AuthRepository
import com.papb.projectakhirandroid.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Start observing local data
        loadProfile()
        
        // Trigger sync from Cloud to Local
        viewModelScope.launch {
            userRepository.fetchUserProfileFromSupabase()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            userRepository.getName().collect { _name.value = it }
        }
        viewModelScope.launch {
            userRepository.getEmail().collect { _email.value = it }
        }
        viewModelScope.launch {
            userRepository.getProfileImageUri().collect { uriString ->
                // Add timestamp only if it's a network URL to bust cache, local URIs don't need it
                _profileImageUri.value = uriString?.let { Uri.parse(it) }
            }
        }
    }

    fun saveProfile(name: String, email: String, imageFile: File?, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true

            // 1. Jika ada gambar baru, upload dulu
            var finalAvatarUrl = _profileImageUri.value?.toString()
            
            if (imageFile != null) {
                val uploadedUrl = userRepository.uploadProfileImage(imageFile)
                if (uploadedUrl != null) {
                    finalAvatarUrl = uploadedUrl
                }
            }

            // 2. Simpan ke DataStore Lokal (Agar UI cepat update)
            userRepository.saveName(name)
            userRepository.saveEmail(email)
            userRepository.saveProfileImageUri(finalAvatarUrl)

            // 3. Simpan ke Database Supabase (Cloud)
            userRepository.upsertUserProfileToSupabase(name, email, finalAvatarUrl)

            _isLoading.value = false
            onSuccess()
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
