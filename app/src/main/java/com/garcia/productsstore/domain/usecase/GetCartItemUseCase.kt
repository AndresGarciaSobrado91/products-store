package com.garcia.productsstore.domain.usecase

import com.garcia.productsstore.domain.model.CartItem
import com.garcia.productsstore.domain.repository.StoreRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCartItemUseCase @Inject constructor(
    private val repository: StoreRepository,
) {
    suspend operator fun invoke(productCode: String): CartItem? = repository.getCartItem(productCode)
}