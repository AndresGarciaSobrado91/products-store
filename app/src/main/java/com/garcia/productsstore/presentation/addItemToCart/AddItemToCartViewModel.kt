package com.garcia.productsstore.presentation.addItemToCart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garcia.productsstore.domain.model.CartItem
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.domain.usecase.CalculateProductFinalPriceUseCase
import com.garcia.productsstore.domain.usecase.GetCartItemUseCase
import com.garcia.productsstore.domain.usecase.RemoveItemFromCartUseCase
import com.garcia.productsstore.domain.usecase.UpdateCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemToCartViewModel @Inject constructor(
    private val getCartItemUseCase: GetCartItemUseCase,
    private val calculateProductFinalPriceUseCase: CalculateProductFinalPriceUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase,
) : ViewModel() {

    private var product: Product? = null
    private var quantity: Int = 1

    data class ViewState(
        val cartItem: CartItem? = null,
        val promoDescription: String? = null,
        val productAlreadyInCart: Boolean = false,
        val showRemoveItemAlert: Boolean = false,
    )

    private val stateMutableLiveData = MutableLiveData(ViewState())
    val stateLiveData = stateMutableLiveData as LiveData<ViewState>

    fun setProduct(product: Product) {
        if (this.product == null) {
            this.product = product
            var productAlreadyInCart = false

            viewModelScope.launch {
                var cartItem = getCartItemUseCase(product.code)
                if (cartItem != null) {
                    quantity = cartItem.quantity
                    productAlreadyInCart = true
                } else {
                    cartItem = calculateProductFinalPriceUseCase(product, quantity)
                }

                stateMutableLiveData.postValue(
                    ViewState(
                        cartItem = cartItem,
                        promoDescription = product.promo?.description,
                        productAlreadyInCart = productAlreadyInCart
                    )
                )
            }
        }
    }

    fun onAddPressed() {
        product?.let {
            quantity++
            val cartItem = calculateProductFinalPriceUseCase(it, quantity)
            stateMutableLiveData.value = stateLiveData.value?.copy(cartItem = cartItem)
        }
    }

    fun onSubtractPressed() {
        product?.let {
            if (quantity > 1) {
                quantity--
                val cartItem = calculateProductFinalPriceUseCase(it, quantity)
                stateMutableLiveData.value = stateLiveData.value?.copy(cartItem = cartItem)
            } else {
                stateMutableLiveData.value = stateLiveData.value?.copy(showRemoveItemAlert = true)
            }
        }
    }

    fun onAddToCartPressed() {
        product?.let {
            viewModelScope.launch {
                updateCartUseCase(it, quantity)
            }
        }
    }

    fun onRemoveItemConfirmation(confirmed: Boolean) {
        if (confirmed) {
            product?.let {
                viewModelScope.launch {
                    removeItemFromCartUseCase(it.code)
                }
            }
        } else {
            stateMutableLiveData.value = stateLiveData.value?.copy(showRemoveItemAlert = false)
        }
    }
}