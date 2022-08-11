package com.garcia.productsstore.domain.model

import java.io.Serializable

data class Product(
    val code: String,
    val name: String,
    val price: Double,
    var promo: Promo? = null,
): Serializable