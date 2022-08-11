package com.garcia.productsstore.data.repository

import com.garcia.productsstore.common.ResultWrapper
import com.garcia.productsstore.data.local.db.AppDatabase
import com.garcia.productsstore.data.remote.client.ProductStoreApi
import com.garcia.productsstore.data.remote.dto.toDomainModel
import com.garcia.productsstore.data.remote.network.ResponseHandler
import com.garcia.productsstore.domain.model.CartItem
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.model.Promo
import com.garcia.productsstore.domain.repository.StoreRepository
import com.garcia.productsstore.domain.utils.DomainObjectMock
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val api: ProductStoreApi,
    database: AppDatabase,
    private val responseHandler: ResponseHandler,
) : StoreRepository {

    private val cartDao = database.cartDao()

    override suspend fun getProducts(): ResultWrapper<List<Product>> {
        return responseHandler {
            api.getProducts().products.map { productDto -> productDto.toDomainModel() }
        }
    }

    override suspend fun getPromos(): ResultWrapper<List<Promo>> =
        ResultWrapper.Success(DomainObjectMock.getPromos()) // let's suppose that there is an available endpoint to get the data

    override fun getCartAsFlow(): Flow<List<CartItem>> = cartDao.getAllAsFlow()

    override suspend fun getCartItem(productCode: String): CartItem? = cartDao.getItem(productCode)

    override suspend fun getCartTotalAsFlow(): Flow<Double?> = cartDao.getCartTotalAsFlow()

    override suspend fun updateCart(item: CartItem) = cartDao.insert(item)

    override suspend fun removeItemFromCart(item: CartItem) = cartDao.delete(item)

    override suspend fun removeItemFromCart(productCode: String) = cartDao.delete(productCode)
}