package com.garcia.productsstore.presentation.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garcia.productsstore.domain.model.Cart
import com.garcia.productsstore.domain.usecase.GetCartUseCase
import com.garcia.productsstore.domain.usecase.RemoveItemFromCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase,
) : ViewModel() {

    data class ViewState(
        val cart: Cart? = null,
        val emptyCart : Boolean = false,
    )

    private val stateMutableLiveData = MutableLiveData(ViewState())
    val stateLiveData = stateMutableLiveData as LiveData<ViewState>

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            getCartUseCase().collect{ cart ->
                stateMutableLiveData.value = stateLiveData.value?.copy(
                    cart = cart,
                    emptyCart = cart.items.isEmpty()
                )
            }
        }
    }

    fun onRemoveItemPressed(productCode: String) {
        viewModelScope.launch {
            removeItemFromCartUseCase(productCode)
        }
    }
}