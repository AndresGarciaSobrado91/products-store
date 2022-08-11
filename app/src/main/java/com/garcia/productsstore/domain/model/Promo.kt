package com.garcia.productsstore.domain.model

import java.io.Serializable

data class Promo(
    val productCode: String,
    val title: String,
    val description: String,
    val price: Double,
    val requiredQuantity: Int,
    val groupSize: Int = 0,  // if 0 -> all items with discount from required quantity
): Serializable
