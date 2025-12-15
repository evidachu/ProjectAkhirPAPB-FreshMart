package com.papb.projectakhirandroid.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papb.projectakhirandroid.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkCurrentUser()
    }

    /**
     * Memeriksa apakah user sudah login saat ViewModel dibuat.
     */
    private fun checkCurrentUser() {
        _uiState.update { it.copy(isLoggedIn = authRepository.isUserLoggedIn()) }
    }

    /**
     * Fungsi untuk melakukan login.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "An unknown error occurred"
                    )
                }
            }
        }
    }

    /**
     * Fungsi untuk melakukan registrasi.
     */
    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.register(email, password)
            if (result.isSuccess) {
                // Setelah register berhasil, anggap user langsung login
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "An unknown error occurred"
                    )
                }
            }
        }
    }

    /**
     * Fungsi untuk melakukan logout.
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update { it.copy(isLoggedIn = false) }
        }
    }

    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)
