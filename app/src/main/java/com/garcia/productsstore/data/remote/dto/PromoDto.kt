package com.garcia.productsstore.data.remote.dto

import com.garcia.productsstore.domain.model.Promo
import com.google.gson.annotations.SerializedName

data class PromoDto(
    @SerializedName("product_code")
    val productCode: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("required_quantity")
    val requiredQuantity: Int,
    @SerializedName("group_size")
    val groupSize: Int?,
)

fun PromoDto.toDomainModel() = Promo(
    productCode = productCode,
    title = title,
    description = description,
    price = price,
    requiredQuantity = requiredQuantity,
    groupSize = groupSize ?: 0,
)