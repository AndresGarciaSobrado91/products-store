package com.garcia.productsstore.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garcia.productsstore.R
import com.garcia.productsstore.common.ResultWrapper
import com.garcia.productsstore.common.Error
import com.garcia.productsstore.common.formatToCurrency
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.usecase.GetCartTotalUseCase
import com.garcia.productsstore.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        val cartTotal: String? = null,
        val error: Error? = null,
    )

    private val _mutableState = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState>
        get() = _mutableState

    init {
        getData()
    }

    fun onSwipeToRefresh() = getData()

    private fun getData() {
        _mutableState.value = ViewState(isLoading = true)
        viewModelScope.launch {
            when (val result = getProductsUseCase()) {
                is ResultWrapper.Error -> _mutableState.value = ViewState(
                    error = Error(
                        resourceId = R.string.unexpected_error_message,
                        message = result.message
                    ),
                    isLoading = false
                )
                ResultWrapper.NetworkError -> _mutableState.value =
                    ViewState(
                        error = Error(resourceId = R.string.connection_error),
                        isLoading = false
                    )
                is ResultWrapper.Success -> _mutableState.value =
                    state.value.copy(
                        products = result.value ?: emptyList(),
                        isLoading = false
                    )
            }

            getCartTotalUseCase().collect { cartTotal ->
                _mutableState.value = state.value.copy(
                    isLoading = false,
                    cartTotal = cartTotal?.formatToCurrency(),
                    error = null
                )
            }
        }
    }
}