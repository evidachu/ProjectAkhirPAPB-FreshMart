package com.papb.projectakhirandroid.domain.usecase.saveproductusecase

import com.papb.projectakhirandroid.data.repository.Repository
import com.papb.projectakhirandroid.domain.model.ProductItem
import javax.inject.Inject

class InsertProductsUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(products: List<ProductItem>) = repository.insertProducts(products)

}