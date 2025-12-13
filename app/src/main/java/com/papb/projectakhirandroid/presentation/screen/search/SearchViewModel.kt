package com.papb.projectakhirandroid.presentation.screen.search

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State // Import State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.domain.usecase.UseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow // Import StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {
    
    private val _searchQuery = mutableStateOf("")
    
    val searchQuery: State<String> = _searchQuery

    private val _searchProductList = MutableStateFlow<List<ProductItem>>(emptyList())
    val searchProductList: StateFlow<List<ProductItem>> = _searchProductList.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchProduct(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isNotEmpty()) {
                useCases.searchProductUseCase.invoke(query).collect { values ->
                    _searchProductList.value = values
                }
            } else {
                _searchProductList.value = emptyList()
            }
        }
    }
}
