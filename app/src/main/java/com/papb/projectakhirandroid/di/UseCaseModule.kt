package com.papb.projectakhirandroid.di

import com.papb.projectakhirandroid.data.repository.Repository
import com.papb.projectakhirandroid.domain.usecase.*
import com.papb.projectakhirandroid.domain.usecase.addcartusecase.AddCartUseCase
import com.papb.projectakhirandroid.domain.usecase.deletecartusecase.DeleteCartUseCase
import com.papb.projectakhirandroid.domain.usecase.getallcartusecase.GetAllCartUseCase
import com.papb.projectakhirandroid.domain.usecase.getallproduct.GetAllProductUseCase
import com.papb.projectakhirandroid.domain.usecase.getselectedproduct.GetSelectedProductUseCase
import com.papb.projectakhirandroid.domain.usecase.readonboarding.ReadOnBoardingUseCase
import com.papb.projectakhirandroid.domain.usecase.saveonboarding.SaveOnBoardingUseCase
import com.papb.projectakhirandroid.domain.usecase.saveproductusecase.InsertProductsUseCase
import com.papb.projectakhirandroid.domain.usecase.searchproductusecase.SearchProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideUseCases(
        repository: Repository
    ): UseCases {
        return UseCases(
            saveOnBoardingUseCase = SaveOnBoardingUseCase(repository),
            insertProductsUseCase = InsertProductsUseCase(repository),
            readOnBoardingUseCase = ReadOnBoardingUseCase(repository),
            getSelectedProductUseCase = GetSelectedProductUseCase(repository),
            getAllProductUseCase = GetAllProductUseCase(repository),
            getAllCartUseCase = GetAllCartUseCase(repository),
            addCartUseCase = AddCartUseCase(repository),
            deleteCartUseCase = DeleteCartUseCase(repository),
            searchProductUseCase = SearchProductUseCase(repository)
        )
    }
}