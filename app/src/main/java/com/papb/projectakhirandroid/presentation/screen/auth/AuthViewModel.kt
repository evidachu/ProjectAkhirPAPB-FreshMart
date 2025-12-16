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
        observeAuthState()
        observeSessionCheck()
    }

    /**
     * Memantau apakah repository sudah selesai melakukan pengecekan session awal.
     */
    private fun observeSessionCheck() {
        viewModelScope.launch {
            authRepository.isReady.collect { isReady ->
                _uiState.update { it.copy(isCheckingSession = !isReady) }
            }
        }
    }

    /**
     * Mengamati perubahan status autentikasi dari Repository.
     */
    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.authState.collect { isLoggedIn ->
                _uiState.update { it.copy(isLoggedIn = isLoggedIn) }
            }
        }
    }

    /**
     * Fungsi untuk melakukan login.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
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
                _uiState.update { it.copy(isLoading = false) }
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

    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isCheckingSession: Boolean = true, // Default true agar UI menunggu check session selesai
    val isLoggedIn: Boolean = false,
    val error: String? = null
)
