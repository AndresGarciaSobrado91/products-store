package com.garcia.productsstore.presentation.checkout.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.garcia.productsstore.R
import com.garcia.productsstore.databinding.ItemCartBinding
import com.garcia.productsstore.domain.model.CartItem

class CartAdapter(
    private val listener: CartAdapterListener,
) : ListAdapter<CartItem, CartAdapter.CartItemViewHolder>(CartItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder =
        CartItemViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener,
        )

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) =
        holder.bind(getItem(position))

    class CartItemViewHolder(
        private val binding: ItemCartBinding,
        private val listener: CartAdapterListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) = with(binding) {
            textViewTitle.text = cartItem.productName
            textViewDetails.text =
                root.resources.getString(
                    R.string.quantity_and_price,
                    cartItem.quantity.toString(),
                    root.resources.getString(
                        R.string.price_label,
                        cartItem.total.toString()
                    )
                )
            textViewGlobalPrice.isVisible = cartItem.discountApplied
            textViewGlobalPrice.text = Html.fromHtml(
                "<del>${
                    root.resources.getString(
                        R.string.price_label,
                        cartItem.globalPrice.toString()
                    )
                }</del>",
                Html.FROM_HTML_MODE_COMPACT
            )
            imageViewRemove.setOnClickListener {
                listener.onRemoveClicked(cartItem.productCode)
            }
        }
    }
}

class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
        oldItem.productCode == newItem.productCode

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
        oldItem == newItem
}

interface CartAdapterListener {
    fun onRemoveClicked(productCode: String)
}