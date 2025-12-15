package com.papb.projectakhirandroid.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papb.projectakhirandroid.data.repository.AuthRepository
import com.papb.projectakhirandroid.navigation.graph.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        _isLoggedIn.value = authRepository.isUserLoggedIn()
    }

    fun onLogout() {
        _isLoggedIn.value = false
    }

    fun onLoginSuccess() {
        _isLoggedIn.value = true
    }
}
