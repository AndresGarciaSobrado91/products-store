package com.garcia.productsstore.domain.model

data class Cart(
    val items: List<CartItem>,
    val total: Double,
)
