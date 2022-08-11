package com.garcia.productsstore.presentation.checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.garcia.productsstore.domain.model.Cart
import com.garcia.productsstore.domain.usecase.GetCartUseCase
import com.garcia.productsstore.domain.usecase.RemoveItemFromCartUseCase
import com.garcia.productsstore.domain.utils.DomainObjectMock
import com.garcia.productsstore.utils.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CheckoutViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CheckoutViewModel

    @MockK(relaxed = true)
    lateinit var getCartUseCase: GetCartUseCase

    @MockK(relaxed = true)
    lateinit var removeItemFromCartUseCase: RemoveItemFromCartUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given initial state, when getCartUseCase returns a cart, verify state`() {
        // given
        mockSuccess()

        // when
        viewModel = CheckoutViewModel(getCartUseCase, removeItemFromCartUseCase)

        // verify
        Assert.assertEquals(
            CheckoutViewModel.ViewState(
                cart = DomainObjectMock.getCart(),
                emptyCart = false,
            ),
            viewModel.stateLiveData.value
        )
    }

    @Test
    fun `given initial state, when getCartUseCase returns an empty cart, verify state`() {
        // given
        val cart = Cart(items = emptyList(), total = 0.0)
        given(getCartResult = cart)

        // when
        viewModel = CheckoutViewModel(getCartUseCase, removeItemFromCartUseCase)

        // verify
        Assert.assertEquals(
            CheckoutViewModel.ViewState(
                cart = cart,
                emptyCart = true,
            ),
            viewModel.stateLiveData.value
        )
    }

    @Test
    fun `given a cart, when removeItemButton pressed, verify state`() {
        // given
        mockSuccess()

        // when
        viewModel = CheckoutViewModel(getCartUseCase, removeItemFromCartUseCase)
        viewModel.onRemoveItemPressed(DomainObjectMock.getProduct().code)

        // verify
        coVerify(exactly = 1) { removeItemFromCartUseCase(any()) }
    }

    // Helpers

    private fun mockSuccess() {
        given(getCartResult = DomainObjectMock.getCart())
    }

    private fun given(
        getCartResult: Cart,
    ) {
        getCartResult.let {
            coEvery { getCartUseCase() } returns flowOf(getCartResult)
        }
    }
}