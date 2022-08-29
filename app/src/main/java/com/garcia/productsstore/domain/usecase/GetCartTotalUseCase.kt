package com.garcia.productsstore.domain.usecase

import com.garcia.productsstore.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCartTotalUseCase @Inject constructor(
    private val repository: StoreRepository,
) {
    suspend operator fun invoke(): Flow<Double?> = repository.getCartTotalAsFlow()
}