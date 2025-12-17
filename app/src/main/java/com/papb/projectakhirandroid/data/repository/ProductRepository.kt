package com.papb.projectakhirandroid.data.repository

import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.utils.DataDummy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor() {

    // This function gets all products from the local DataDummy.
    fun getAllProducts(): List<ProductItem> {
        return DataDummy.generateDummyProduct()
    }

    // This function finds a product by its ID from the dummy list.
    fun getSelectedProduct(id: Int): ProductItem? {
        return DataDummy.generateDummyProduct().find { it.id == id }
    }
    
    // This function is a placeholder since we are not using the local Room database for products anymore.
    // It's called when adding to cart in DetailViewModel. We'll leave it empty to prevent crashes.
    suspend fun insertProduct(productItem: ProductItem) {
        // No-op
    }
}
