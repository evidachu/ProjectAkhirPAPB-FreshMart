package com.papb.projectakhirandroid.presentation.screen.collection

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Move data class here or to a dedicated model file
data class CollectionItem(val id: Long, val name: String, val imageUri: Uri?)

@HiltViewModel
class CollectionViewModel @Inject constructor() : ViewModel() {

    private val _collections = MutableStateFlow<List<CollectionItem>>(emptyList())
    val collections = _collections.asStateFlow()

    fun addCollection(name: String, imageUri: Uri?) {
        val newItem = CollectionItem(
            id = System.currentTimeMillis(), // Simple unique ID
            name = name,
            imageUri = imageUri
        )
        _collections.value = _collections.value + newItem
    }

    fun deleteCollection(item: CollectionItem) {
        _collections.value = _collections.value - item
    }
}