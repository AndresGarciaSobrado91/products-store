package com.garcia.productsstore.domain.usecase

import com.garcia.productsstore.domain.model.CartItem
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.model.Promo
import com.garcia.productsstore.domain.utils.DomainObjectMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CalculateProductFinalPriceUseCaseTest{

    private lateinit var useCase: CalculateProductFinalPriceUseCase

    @Before
    fun setUp() {
        useCase = CalculateProductFinalPriceUseCase()
    }

    @Test
    fun `given specific product and promo, should return an appropriate cartItem`() {
        // given
        val product = Product(
            code = DomainObjectMock.VOUCHER_CODE,
            name = "Cabify Voucher",
            price = 5.0,
            promo = Promo(
                productCode = DomainObjectMock.VOUCHER_CODE,
                title = "2 for 1",
                description = "Buy two of the same product, get one free!",
                price = 2.5,
                requiredQuantity = 2,
                groupSize = 2,
            ),
        )
        val quantity = 5

        // when
        val result = useCase(product,quantity)

        // then
        Assert.assertEquals(
            CartItem(
                productCode = product.code,
                productName = product.name,
                productPrice = product.price,
                quantity = quantity,
                globalPrice = product.price * quantity,
                discountApplied = true,
                total = 15.0
            ),
            result
        )
    }

    @Test
    fun `given specific product and NO promo, should return an appropriate cartItem`() {
        // given
        val product = Product(
            code = DomainObjectMock.VOUCHER_CODE,
            name = "Cabify Voucher",
            price = 5.0,
            promo = null,
        )
        val quantity = 5

        // when
        val result = useCase(product,quantity)

        // then
        Assert.assertEquals(
            CartItem(
                productCode = product.code,
                productName = product.name,
                productPrice = product.price,
                quantity = quantity,
                globalPrice = product.price * quantity,
                discountApplied = false,
                total = 25.0
            ),
            result
        )
    }
}