package com.papb.projectakhirandroid.presentation.screen.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.domain.usecase.getallcartusecase.GetAllCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val getAllCartUseCase: GetAllCartUseCase
) : ViewModel() {

    private val _productCartList = MutableStateFlow<List<ProductItem>>(emptyList())
    val productCartList = _productCartList.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice = _totalPrice.asStateFlow()

    init {
        getCartItems()
    }

    private fun getCartItems() {
        viewModelScope.launch {
            getAllCartUseCase.invoke(isCart = true).collectLatest {
                _productCartList.value = it
                calculateTotalPrice(it)
            }
        }
    }

    private fun calculateTotalPrice(items: List<ProductItem>) {
        var total = 0.0
        items.forEach {
            total += it.price * it.quantity
        }
        _totalPrice.value = total
    }
}