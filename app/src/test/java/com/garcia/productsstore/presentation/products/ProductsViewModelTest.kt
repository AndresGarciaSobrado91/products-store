package com.garcia.productsstore.presentation.products

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.garcia.productsstore.R
import com.garcia.productsstore.common.Error
import com.garcia.productsstore.common.ResultWrapper
import com.garcia.productsstore.common.formatToCurrency
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.usecase.GetCartTotalUseCase
import com.garcia.productsstore.domain.usecase.GetProductsUseCase
import com.garcia.productsstore.domain.utils.DomainObjectMock
import com.garcia.productsstore.utils.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProductsViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProductsViewModel

    @MockK(relaxed = true)
    lateinit var getProductsUseCase: GetProductsUseCase

    @MockK(relaxed = true)
    lateinit var getCartTotalUseCase: GetCartTotalUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given initial state, when getProductsUseCase succeed, verify state`() {
        // given
        mockSuccess()

        // when
        viewModel = ProductsViewModel(getProductsUseCase, getCartTotalUseCase)

        // verify
        Assert.assertEquals(
            ProductsViewModel.ViewState(
                isLoading = false,
                products = DomainObjectMock.getProducts(),
                cartTotal = CART_TOTAL.formatToCurrency(),
                error = null,
            ),
            viewModel.stateLiveData.value
        )
    }

    @Test
    fun `given initial state, when getProductsUseCase fails with Error, verify state`() {
        // given
        val error = ResultWrapper.Error(message = "Error message")
        given(getProductsResult = error)

        // when
        viewModel = ProductsViewModel(getProductsUseCase, getCartTotalUseCase)

        // verify
        Assert.assertEquals(
            ProductsViewModel.ViewState(
                isLoading = false,
                products = emptyList(),
                cartTotal = null,
                error = Error(
                    resourceId = R.string.unexpected_error_message,
                    message = error.message
                ),
            ),
            viewModel.stateLiveData.value
        )
    }

    @Test
    fun `given initial state, when getProductsUseCase fails with NetworkError, verify state`() {
        // given
        val error = ResultWrapper.NetworkError
        given(getProductsResult = error)

        // when
        viewModel = ProductsViewModel(getProductsUseCase, getCartTotalUseCase)

        // verify
        Assert.assertEquals(
            ProductsViewModel.ViewState(
                isLoading = false,
                products = emptyList(),
                cartTotal = null,
                error = Error(
                    resourceId = R.string.connection_error,
                ),
            ),
            viewModel.stateLiveData.value
        )
    }

    private fun mockSuccess() {
        given(
            getProductsResult = ResultWrapper.Success(DomainObjectMock.getProducts()),
            getCartTotalResult = CART_TOTAL,
        )
    }

    private fun given(
        getProductsResult: ResultWrapper<List<Product>>? = null,
        getCartTotalResult: Double? = null,
    ) {
        getProductsResult?.let {
            coEvery { getProductsUseCase() } returns flowOf(getProductsResult)
        }

        getCartTotalResult?.let {
            coEvery { getCartTotalUseCase() } returns flowOf(getCartTotalResult)
        }
    }

    companion object{
        const val CART_TOTAL = 20.00
    }
}