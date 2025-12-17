package com.papb.projectakhirandroid.presentation.screen.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papb.projectakhirandroid.data.repository.CollectionRepository
import com.papb.projectakhirandroid.domain.model.CollectionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val repository: CollectionRepository
) : ViewModel() {

    private val _collections = MutableStateFlow<List<CollectionItem>>(emptyList())
    val collections = _collections.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadCollections()
    }

    fun loadCollections() {
        viewModelScope.launch {
            _isLoading.value = true
            _collections.value = repository.getCollections()
            _isLoading.value = false
        }
    }

    fun addCollection(name: String, imageFile: File?) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.addCollection(name, imageFile)
            if (success) {
                loadCollections()
            }
            _isLoading.value = false
        }
    }

    fun deleteCollection(item: CollectionItem) {
        viewModelScope.launch {
            val success = repository.deleteCollection(item.id)
            if (success) {
                // Optimistic update or reload
                _collections.value = _collections.value.filter { it.id != item.id }
            }
        }
    }
}
