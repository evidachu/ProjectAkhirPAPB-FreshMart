package com.papb.projectakhirandroid.presentation.screen.about

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papb.projectakhirandroid.data.repository.AuthRepository
import com.papb.projectakhirandroid.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
                _profileImageUri.value = uriString?.let { Uri.parse(it) }
            }
        }
    }

    fun saveProfile(name: String, email: String) {
        viewModelScope.launch {
            // 1. Simpan ke DataStore Lokal dulu (Agar UI cepat update)
            userRepository.saveName(name)
            userRepository.saveEmail(email)
            
            // Image upload has been removed, so we only handle name and email
            // We use the existing profileImageUri if it exists in DataStore/State
            val currentImageUriString = _profileImageUri.value?.toString()

            // 2. Simpan ke Database Supabase (Cloud)
            // Kita kirim data final (Nama, Email, dan URL Gambar yg sudah ada) ke tabel profiles
            userRepository.upsertUserProfileToSupabase(name, email, currentImageUriString)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
