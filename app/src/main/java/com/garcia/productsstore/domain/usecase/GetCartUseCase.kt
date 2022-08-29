package com.garcia.productsstore.domain.usecase

import com.garcia.productsstore.domain.model.Cart
import com.garcia.productsstore.domain.repository.StoreRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCartUseCase @Inject constructor(
    private val repository: StoreRepository,
) {
    operator fun invoke(): Flow<Cart> =
        repository.getCartAsFlow().map {
            val total = repository.getCartTotalAsFlow().firstOrNull()
            Cart(items = it, total = total ?: 0.0)
        }
}