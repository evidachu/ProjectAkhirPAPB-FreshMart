package com.papb.projectakhirandroid.presentation.screen.about

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papb.projectakhirandroid.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri

    init {
        loadProfile()
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

    fun saveProfile(name: String, email: String, imageUri: Uri?) {
        viewModelScope.launch {
            userRepository.saveName(name)
            userRepository.saveEmail(email)
            userRepository.saveProfileImageUri(imageUri?.toString())
            loadProfile() // Reload data after saving
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
