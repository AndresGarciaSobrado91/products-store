package com.garcia.productsstore.domain.repository

import com.garcia.productsstore.common.ResultWrapper
import com.garcia.productsstore.domain.model.CartItem
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.model.Promo
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    suspend fun getProducts(): ResultWrapper<List<Product>>

    suspend fun getPromos(): ResultWrapper<List<Promo>>

    fun getCartAsFlow(): Flow<List<CartItem>>

    suspend fun getCartItem(productCode: String): CartItem?

    suspend fun getCartTotalAsFlow(): Flow<Double?>

    suspend fun updateCart(item: CartItem)

    suspend fun removeItemFromCart(item: CartItem)

    suspend fun removeItemFromCart(productCode: String)
}