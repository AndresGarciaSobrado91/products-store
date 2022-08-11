package com.garcia.productsstore.data.remote.dto

import com.garcia.productsstore.domain.model.Product
import com.google.gson.annotations.SerializedName

data class ProductsDto(
    @SerializedName("products")
    val products: List<ProductDto>
) {
    data class ProductDto(
        @SerializedName("code")
        val code: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: Double,
    )
}

fun ProductsDto.ProductDto.toDomainModel() = Product(code = code, name = name, price = price)