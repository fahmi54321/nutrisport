package com.nutrisport.data.domain

import com.nutrisport.shared.domain.Product
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getCurrentUserId(): String?
    fun readDiscountedProductsFlow(): Flow<RequestState<List<Product>>>
    fun readNewProductsFlow(): Flow<RequestState<List<Product>>>
    fun readProductByIdFlow(id: String): Flow<RequestState<Product>>
    fun readProductByIdsFlow(ids: List<String>): Flow<RequestState<List<Product>>>
    fun readProductByCategoryFlow(category: ProductCategory): Flow<RequestState<List<Product>>>
}