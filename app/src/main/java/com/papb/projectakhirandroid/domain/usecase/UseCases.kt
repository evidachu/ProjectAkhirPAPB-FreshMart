package com.papb.projectakhirandroid.domain.usecase

import com.papb.projectakhirandroid.domain.usecase.addcartusecase.AddCartUseCase
import com.papb.projectakhirandroid.domain.usecase.deletecartusecase.DeleteCartUseCase
import com.papb.projectakhirandroid.domain.usecase.getallcartusecase.GetAllCartUseCase
import com.papb.projectakhirandroid.domain.usecase.getallproduct.GetAllProductUseCase
import com.papb.projectakhirandroid.domain.usecase.getselectedproduct.GetSelectedProductUseCase
import com.papb.projectakhirandroid.domain.usecase.readonboarding.ReadOnBoardingUseCase
import com.papb.projectakhirandroid.domain.usecase.saveonboarding.SaveOnBoardingUseCase
import com.papb.projectakhirandroid.domain.usecase.saveproductusecase.InsertProductsUseCase
import com.papb.projectakhirandroid.domain.usecase.searchproductusecase.SearchProductUseCase

data class UseCases(
    val saveOnBoardingUseCase: SaveOnBoardingUseCase,
    val insertProductsUseCase: InsertProductsUseCase,
    val readOnBoardingUseCase: ReadOnBoardingUseCase,
    val getSelectedProductUseCase: GetSelectedProductUseCase,
    val getAllProductUseCase: GetAllProductUseCase,
    val getAllCartUseCase: GetAllCartUseCase,
    val addCartUseCase: AddCartUseCase,
    val deleteCartUseCase: DeleteCartUseCase,
    val searchProductUseCase: SearchProductUseCase
)