package com.garcia.productsstore.domain.usecase

import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.repository.StoreRepository
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(
    private val repository: StoreRepository,
    private val calculateProductFinalPrice: CalculateProductFinalPriceUseCase,
) {
    suspend operator fun invoke(product: Product, quantity: Int) {
        val cartItem = calculateProductFinalPrice(product, quantity)
        repository.updateCart(cartItem)
    }
}