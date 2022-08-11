package com.garcia.productsstore.domain.usecase

import com.garcia.productsstore.domain.model.CartItem
import com.garcia.productsstore.domain.model.Product

class CalculateProductFinalPriceUseCase {

    operator fun invoke(product: Product, quantity: Int): CartItem {
        var discountApplied = false
        var total = quantity * product.price

        product.promo?.let { promo ->
            if (quantity >= promo.requiredQuantity) {
                if (promo.groupSize > 0) {
                    val itemsWithDiscount = (quantity / promo.groupSize) * promo.groupSize
                    if (itemsWithDiscount > 0) {
                        discountApplied = true
                    }
                    val itemsWithoutDiscount = quantity - itemsWithDiscount
                    total =
                        (itemsWithDiscount * promo.price) + (itemsWithoutDiscount * product.price)
                } else if (promo.groupSize == 0) {
                    total = promo.price * quantity
                    discountApplied = true
                }
            }
        }

        return CartItem(
            productCode = product.code,
            productName = product.name,
            productPrice = product.price,
            quantity = quantity,
            globalPrice = product.price * quantity,
            discountApplied = discountApplied,
            total = total,
        )
    }
}