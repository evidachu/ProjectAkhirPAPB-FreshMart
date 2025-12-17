package com.papb.projectakhirandroid.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papb.projectakhirandroid.data.repository.ProductRepository
import com.papb.projectakhirandroid.data.repository.ReviewRepository
import com.papb.projectakhirandroid.data.repository.UserRepository
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.domain.model.Review
import com.papb.projectakhirandroid.domain.usecase.UseCases
import com.papb.projectakhirandroid.utils.Constants.PRODUCT_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedProduct = MutableStateFlow<ProductItem?>(null)
    val selectedProduct: StateFlow<ProductItem?> = _selectedProduct

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    init {
        savedStateHandle.get<Int>(PRODUCT_ARGUMENT_KEY)?.let { productId ->
            getProductById(productId)
            loadReviews(productId)
        }
        getCurrentUserId()
    }

    private fun getCurrentUserId() {
        viewModelScope.launch {
            _currentUserId.value = userRepository.getCurrentUserId()
        }
    }

    private fun getProductById(id: Int) {
        viewModelScope.launch {
            _selectedProduct.value = productRepository.getSelectedProduct(id)
        }
    }

    fun loadReviews(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _reviews.value = reviewRepository.getReviews(productId)
            _isLoading.value = false
        }
    }

    fun submitReview(productId: Int, rating: Int, reviewText: String, imageFile: File?) {
        viewModelScope.launch {
            _isLoading.value = true
            val username = userRepository.getName().first()
            val profilePicUrl = userRepository.getProfileImageUri().first()

            val success = reviewRepository.addReview(
                productId = productId,
                rating = rating,
                reviewText = reviewText,
                username = username,
                userProfilePicUrl = profilePicUrl,
                imageFile = imageFile
            )

            if (success) {
                loadReviews(productId) // Refresh reviews after submission
            }
            _isLoading.value = false
        }
    }

    fun updateReview(reviewId: Long, productId: Int, rating: Int, reviewText: String, existingImageUrl: String?, newImageFile: File?) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = reviewRepository.updateReview(
                reviewId = reviewId,
                productId = productId,
                rating = rating,
                reviewText = reviewText,
                existingImageUrl = existingImageUrl,
                newImageFile = newImageFile
            )

            if (success) {
                loadReviews(productId)
            }
            _isLoading.value = false
        }
    }

    fun deleteReview(review: Review) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = reviewRepository.deleteReview(review.id)
            if (success) {
                loadReviews(review.productId)
            }
            _isLoading.value = false
        }
    }

    fun addCart(productItem: ProductItem) {
        viewModelScope.launch {
            // Using UseCase to ensure it saves to the correct local data source (Room)
            useCases.addCartUseCase.invoke(productItem)
        }
    }
}
