package com.garcia.productsstore.presentation.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garcia.productsstore.R
import com.garcia.productsstore.common.Error
import com.garcia.productsstore.common.ResultWrapper
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.usecase.GetCartTotalUseCase
import com.garcia.productsstore.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getCartTotalUseCase: GetCartTotalUseCase,
) : ViewModel() {

    data class ViewState(
        val isLoading: Boolean = false,
        val products: List<Product> = emptyList(),
        val cartTotal: Double? = null,
        val error: Error? = null,
    )

    private val stateMutableLiveData = MutableLiveData(ViewState())
    val stateLiveData = stateMutableLiveData as LiveData<ViewState>

    init {
        getData()
    }

    fun onSwipeToRefresh() = getData()

    private fun getData() {
        stateMutableLiveData.value = ViewState(isLoading = true)
        viewModelScope.launch {
            getProductsUseCase().collect { result ->
                when (result) {
                    is ResultWrapper.Error -> stateMutableLiveData.value = ViewState(
                        error = Error(
                            resourceId = R.string.unexpected_error_message,
                            message = result.message
                        ),
                        isLoading = false
                    )
                    ResultWrapper.NetworkError -> stateMutableLiveData.value =
                        ViewState(
                            error = Error(resourceId = R.string.connection_error),
                            isLoading = false
                        )
                    is ResultWrapper.Success -> stateMutableLiveData.value =
                        stateLiveData.value?.copy(
                            products = result.value ?: emptyList(),
                            isLoading = false
                        )
                }
            }

            getCartTotalUseCase().collect { cartTotal ->
                stateMutableLiveData.value = stateLiveData.value?.copy(
                    isLoading = false,
                    cartTotal = cartTotal,
                    error = null
                )
            }
        }
    }
}