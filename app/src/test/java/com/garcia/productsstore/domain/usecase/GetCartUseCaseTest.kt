package com.garcia.productsstore.domain.usecase

import com.garcia.productsstore.domain.model.Cart
import com.garcia.productsstore.domain.model.CartItem
import com.garcia.productsstore.domain.repository.StoreRepository
import com.garcia.productsstore.domain.utils.DomainObjectMock
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetCartUseCaseTest {
    private lateinit var useCase: GetCartUseCase

    @MockK
    internal lateinit var repository: StoreRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetCartUseCase(repository)
        mockSuccess()
    }

    @Test
    fun `given cached data, when use case invoked, should return correct data`() = runTest {
        // when
        val result = useCase().first()

        // then
        Assert.assertEquals(
            Cart(
                items = cartItemList,
                total = 67.0
            ),
            result
        )
    }

    // Helpers

    private fun mockSuccess() {
        given(
            cartResult = flowOf(cartItemList),
            cartTotalResult = flowOf(cartItemList.sumOf { it.total }),
        )
    }

    private fun given(
        cartResult: Flow<List<CartItem>>? = null,
        cartTotalResult: Flow<Double>? = null,
    ) {
        cartResult?.let {
            coEvery { repository.getCartAsFlow() } returns it
        }
        cartTotalResult?.let {
            coEvery { repository.getCartTotalAsFlow() } returns it
        }
    }

    private val cartItemList = listOf(
        CartItem(
            productCode = DomainObjectMock.VOUCHER_CODE,
            productName = "Cabify Voucher",
            productPrice = 5.0,
            quantity = 3,
            globalPrice = 15.0,
            discountApplied = true,
            total = 10.0
        ),
        CartItem(
            productCode = DomainObjectMock.TSHIRT_CODE,
            productName = "Cabify T-Shirt",
            productPrice = 20.0,
            quantity = 3,
            globalPrice = 60.0,
            discountApplied = true,
            total = 57.0
        )
    )
}