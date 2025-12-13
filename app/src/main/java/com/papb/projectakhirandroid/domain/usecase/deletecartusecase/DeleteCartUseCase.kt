package com.papb.projectakhirandroid.domain.usecase.deletecartusecase

import com.papb.projectakhirandroid.data.repository.Repository
import com.papb.projectakhirandroid.domain.model.ProductItem
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(productItem: ProductItem) = repository.deleteCart(productItem)

}