package com.garcia.productsstore.domain.usecase

import com.garcia.productsstore.common.ResultWrapper
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: StoreRepository,
) {
    operator fun invoke(): Flow<ResultWrapper<List<Product>>> = flow {
        val productsResponse = repository.getProducts()

        when (val discountsResponse = repository.getPromos()) {
            is ResultWrapper.Error -> {
                emit(ResultWrapper.Error(discountsResponse.code, discountsResponse.message))
                return@flow
            }
            ResultWrapper.NetworkError -> {
                emit(ResultWrapper.NetworkError)
                return@flow
            }
            is ResultWrapper.Success -> {
                if (productsResponse is ResultWrapper.Success) {
                    discountsResponse.value?.forEach {
                        val productCode = it.productCode
                        val product =
                            productsResponse.value?.firstOrNull { product -> product.code == productCode }
                        product?.promo = it
                    }
                }
            }
        }

        emit(productsResponse)
    }
}