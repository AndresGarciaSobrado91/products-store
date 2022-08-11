package com.garcia.productsstore.presentation.addItemToCart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.garcia.productsstore.domain.model.CartItem
import com.garcia.productsstore.domain.usecase.CalculateProductFinalPriceUseCase
import com.garcia.productsstore.domain.usecase.GetCartItemUseCase
import com.garcia.productsstore.domain.usecase.RemoveItemFromCartUseCase
import com.garcia.productsstore.domain.usecase.UpdateCartUseCase
import com.garcia.productsstore.domain.utils.DomainObjectMock
import com.garcia.productsstore.utils.CoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AddItemToCartViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddItemToCartViewModel

    @MockK(relaxed = true)
    lateinit var getCartItemUseCase: GetCartItemUseCase

    @MockK(relaxed = true)
    lateinit var calculateProductFinalPriceUseCase: CalculateProductFinalPriceUseCase

    @MockK(relaxed = true)
    lateinit var updateCartUseCase: UpdateCartUseCase

    @MockK(relaxed = true)
    lateinit var removeItemFromCartUseCase: RemoveItemFromCartUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = AddItemToCartViewModel(
            getCartItemUseCase,
            calculateProductFinalPriceUseCase,
            updateCartUseCase,
            removeItemFromCartUseCase
        )
    }

    @Test
    fun `given cartItem stored in DB, when product set, verify state`() {
        // given
        mockSuccess()

        // when
        viewModel.setProduct(DomainObjectMock.getProduct())

        // verify
        Assert.assertEquals(
            AddItemToCartViewModel.ViewState(
                cartItem = DomainObjectMock.getCartItem(),
                promoDescription = DomainObjectMock.getProduct().promo?.description,
                productAlreadyInCart = true,
                showRemoveItemAlert = false,
            ),
            viewModel.stateLiveData.value
        )
    }

    @Test
    fun `given cartItem NOT stored in DB, when product set, verify state`() {
        // given
        given(calculateProductFinalPriceResult = DomainObjectMock.getCartItem())

        // when
        viewModel.setProduct(DomainObjectMock.getProduct())

        // verify
        Assert.assertEquals(
            AddItemToCartViewModel.ViewState(
                cartItem = DomainObjectMock.getCartItem(),
                promoDescription = DomainObjectMock.getProduct().promo?.description,
                productAlreadyInCart = false,
                showRemoveItemAlert = false,
            ),
            viewModel.stateLiveData.value
        )
    }

    @Test
    fun `given a cartItem, when addButton pressed, verify state`() {
        // given
        val cartItem = DomainObjectMock.getCartItem()
        val calculatedCartItem = cartItem.copy(
            quantity = cartItem.quantity++,
            globalPrice = 30.0
        )
        given(
            getCartItemResult = cartItem,
            calculateProductFinalPriceResult = calculatedCartItem
        )

        // when
        viewModel.setProduct(DomainObjectMock.getProduct())
        viewModel.onAddPressed()

        // verify
        Assert.assertEquals(
            AddItemToCartViewModel.ViewState(
                cartItem = calculatedCartItem,
                promoDescription = DomainObjectMock.getProduct().promo?.description,
                productAlreadyInCart = true,
                showRemoveItemAlert = false,
            ),
            viewModel.stateLiveData.value
        )
    }

    @Test
    fun `given a cartItem, when subtractButton pressed, verify state`() {
        // given
        val cartItem = DomainObjectMock.getCartItem()
        val calculatedCartItem = cartItem.copy(
            quantity = cartItem.quantity--,
            globalPrice = 20.0,
            total = 10.0,
        )
        given(
            getCartItemResult = cartItem,
            calculateProductFinalPriceResult = calculatedCartItem
        )

        // when
        viewModel.setProduct(DomainObjectMock.getProduct())
        viewModel.onSubtractPressed()

        // verify
        Assert.assertEquals(
            AddItemToCartViewModel.ViewState(
                cartItem = calculatedCartItem,
                promoDescription = DomainObjectMock.getProduct().promo?.description,
                productAlreadyInCart = true,
                showRemoveItemAlert = false,
            ),
            viewModel.stateLiveData.value
        )
    }

    @Test
    fun `given a cartItem with quantity equals to 1, when subtractButton pressed, verify state`() {
        // given
        val cartItem = DomainObjectMock.getCartItem()
        cartItem.quantity = 1
        given(getCartItemResult = cartItem)

        // when
        viewModel.setProduct(DomainObjectMock.getProduct())
        viewModel.onSubtractPressed()

        // verify
        Assert.assertEquals(
            AddItemToCartViewModel.ViewState(
                cartItem = cartItem,
                promoDescription = DomainObjectMock.getProduct().promo?.description,
                productAlreadyInCart = true,
                showRemoveItemAlert = true,
            ),
            viewModel.stateLiveData.value
        )
    }

    @Test
    fun `given a cartItem, when addToCartButton pressed, verify correct use case is invoked`() {
        // given
        val cartItem = DomainObjectMock.getCartItem()
        given(getCartItemResult = cartItem)

        // when
        viewModel.setProduct(DomainObjectMock.getProduct())
        viewModel.onAddToCartPressed()

        // verify
        coVerify(exactly = 1) { updateCartUseCase.invoke(any(),any()) }
    }

    @Test
    fun `given a cartItem, when removeItemConfirmation triggered as false, verify use case is NOT invoked`() {
        // given
        val cartItem = DomainObjectMock.getCartItem()
        given(getCartItemResult = cartItem)

        // when
        viewModel.setProduct(DomainObjectMock.getProduct())
        viewModel.onRemoveItemConfirmation(false)

        // verify
        coVerify(exactly = 0) { removeItemFromCartUseCase.invoke(any()) }
    }

    @Test
    fun `given a cartItem, when removeItemConfirmation triggered as false, verify state`() {
        // given
        val cartItem = DomainObjectMock.getCartItem()
        given(getCartItemResult = cartItem)

        // when
        viewModel.setProduct(DomainObjectMock.getProduct())
        viewModel.onRemoveItemConfirmation(false)

        // verify
        Assert.assertEquals(
            AddItemToCartViewModel.ViewState(
                cartItem = cartItem,
                promoDescription = DomainObjectMock.getProduct().promo?.description,
                productAlreadyInCart = true,
                showRemoveItemAlert = false,
            ),
            viewModel.stateLiveData.value
        )
    }

    @Test
    fun `given a cartItem, when removeItemConfirmation triggered as true, verify use case is invoked`() {
        // given
        val cartItem = DomainObjectMock.getCartItem()
        given(getCartItemResult = cartItem)

        // when
        viewModel.setProduct(DomainObjectMock.getProduct())
        viewModel.onRemoveItemConfirmation(true)

        // verify
        coVerify(exactly = 1) { removeItemFromCartUseCase.invoke(any()) }
    }

    // Helpers

    private fun mockSuccess() {
        given(
            getCartItemResult = DomainObjectMock.getCartItem(),
            calculateProductFinalPriceResult = DomainObjectMock.getCartItem(),
        )
        coJustRun { updateCartUseCase(any(), any()) }
        coJustRun { removeItemFromCartUseCase(any()) }
    }

    private fun given(
        getCartItemResult: CartItem? = null,
        calculateProductFinalPriceResult: CartItem? = null,
    ) {

        coEvery { getCartItemUseCase(any()) } returns getCartItemResult

        calculateProductFinalPriceResult?.let {
            coEvery {
                calculateProductFinalPriceUseCase(
                    any(),
                    any()
                )
            } returns calculateProductFinalPriceResult
        }
    }
}