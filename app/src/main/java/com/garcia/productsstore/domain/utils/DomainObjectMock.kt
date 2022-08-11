package com.garcia.productsstore.domain.utils

import com.garcia.productsstore.domain.model.Cart
import com.garcia.productsstore.domain.model.CartItem
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.model.Promo

object DomainObjectMock {

    const val VOUCHER_CODE = "VOUCHER"
    const val TSHIRT_CODE = "TSHIRT"
    const val MUG_CODE = "MUG"

    fun getPromos() = listOf(
        Promo(
            productCode = VOUCHER_CODE,
            title = "2 for 1",
            description = "Buy two of the same product, get one free!",
            price = 2.5,
            requiredQuantity = 2,
            groupSize = 2,
        ),
        Promo(
            productCode = TSHIRT_CODE,
            title = "Bulk purchase",
            description = "Buying three or more of this product, the price is reduced to 19.00 €!",
            price = 19.0,
            requiredQuantity = 3,
            groupSize = 0,
        )
    )

    fun getProducts(): List<Product> = listOf(
        Product(
            code = VOUCHER_CODE,
            name = "Cabify Voucher",
            price = 5.0,
            promo = null,
        ),
        Product(
            code = TSHIRT_CODE,
            name = "Cabify T-Shirt",
            price = 20.0,
            promo = null,
        ),
        Product(
            code = MUG_CODE,
            name = "Cabify Coffee Mug",
            price = 7.5,
            promo = null,
        ),
    )

    fun getProduct(): Product =
        Product(
            code = VOUCHER_CODE,
            name = "Cabify Voucher",
            price = 5.0,
            promo = getPromos().first(),
        )

    fun getCartItem(): CartItem =
        CartItem(
            productCode = VOUCHER_CODE,
            productName = "Cabify Voucher",
            productPrice = 5.0,
            quantity = 5,
            globalPrice = 25.0,
            discountApplied = true,
            total = 15.0
        )

    fun getCart(): Cart =
        Cart(
            items = listOf(getCartItem()),
            total = getCartItem().total
        )
}