package com.papb.projectakhirandroid.domain.usecase.getallcartusecase

import com.papb.projectakhirandroid.data.repository.Repository
import com.papb.projectakhirandroid.domain.model.ProductItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCartUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(isCart: Boolean = true): Flow<List<ProductItem>> =
        repository.getAllProductCart(isCart)

}
