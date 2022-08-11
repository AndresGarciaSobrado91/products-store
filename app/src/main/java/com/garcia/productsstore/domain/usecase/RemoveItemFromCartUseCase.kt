package com.garcia.productsstore.domain.usecase

import com.garcia.productsstore.domain.repository.StoreRepository
import javax.inject.Inject

class RemoveItemFromCartUseCase @Inject constructor(
    private val repository: StoreRepository,
) {

    suspend operator fun invoke(productCode: String) = repository.removeItemFromCart(productCode)
}