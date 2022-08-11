package com.garcia.productsstore.data.remote.client

import com.garcia.productsstore.data.remote.dto.ProductsDto
import retrofit2.http.GET

interface ProductStoreApi {
    @GET("Products.json")
    suspend fun getProducts(): ProductsDto
}