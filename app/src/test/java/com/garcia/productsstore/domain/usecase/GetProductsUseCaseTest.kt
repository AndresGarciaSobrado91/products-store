package com.garcia.productsstore.domain.usecase

import com.garcia.productsstore.common.ResultWrapper
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.model.Promo
import com.garcia.productsstore.domain.repository.StoreRepository
import com.garcia.productsstore.domain.utils.DomainObjectMock
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class GetProductsUseCaseTest{

    private lateinit var useCase: GetProductsUseCase

    @MockK
    internal lateinit var repository: StoreRepository

    @Before
    internal fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetProductsUseCase(repository)
        mockSuccess()
    }

    @Test
    fun `should call getProducts once`() = runTest {
        // when
        useCase().first()

        // then
        coVerify(exactly = 1){ repository.getProducts() }
    }


    @Test
    fun `should call getPromos once`() = runTest {
        // when
        useCase().first()

        // then
        coVerify(exactly = 1){ repository.getPromos() }
    }

    @Test
    fun `when repo calls are successful, should return Success`() = runTest {
        // when
        val result = useCase().first()

        // then
        Assert.assertTrue(result is ResultWrapper.Success<*>)
    }

    @Test
    fun `when products calls fails, should return Error`() = runTest {
        // given
        given(productResult = ResultWrapper.Error(404,"Not found"))

        // when
        val result = useCase().first()

        // then
        Assert.assertTrue(result is ResultWrapper.Error)
        Assert.assertEquals(404, (result as ResultWrapper.Error).code)
        Assert.assertEquals("Not found", (result as ResultWrapper.Error).message)
    }

    @Test
    fun `when promos calls fails, should return Error`() = runTest {
        // given
        given(promoResult = ResultWrapper.Error(404,"Not found"))

        // when
        val result = useCase().first()

        // then
        Assert.assertTrue(result is ResultWrapper.Error)
        Assert.assertEquals(404, (result as ResultWrapper.Error).code)
        Assert.assertEquals("Not found", (result as ResultWrapper.Error).message)
    }

    @Test
    fun `when products calls fails with NetworkError, should return NetworkError`() = runTest {
        // given
        given(productResult = ResultWrapper.NetworkError)

        // when
        val result = useCase().first()

        // then
        Assert.assertTrue(result is ResultWrapper.NetworkError)
    }

    @Test
    fun `when promos calls fails with NetworkError, should return NetworkError`() = runTest {
        // given
        given(promoResult = ResultWrapper.NetworkError)

        // when
        val result = useCase().first()

        // then
        Assert.assertTrue(result is ResultWrapper.NetworkError)
    }

    // Helpers

    private fun mockSuccess(){
        given(
            productResult = ResultWrapper.Success(DomainObjectMock.getProducts()),
            promoResult = ResultWrapper.Success(DomainObjectMock.getPromos()),
        )
    }

    private fun given(
        productResult: ResultWrapper<List<Product>>? = null,
        promoResult: ResultWrapper<List<Promo>>? = null,
    ){
        productResult?.let {
            coEvery { repository.getProducts() } returns it
        }
        promoResult?.let {
            coEvery { repository.getPromos() } returns it
        }
    }
}