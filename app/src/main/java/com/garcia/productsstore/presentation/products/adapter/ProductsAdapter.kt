package com.garcia.productsstore.presentation.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.garcia.productsstore.R
import com.garcia.productsstore.common.formatToCurrency
import com.garcia.productsstore.databinding.ItemProductBinding
import com.garcia.productsstore.domain.model.Product

class ProductsAdapter(
    private val listener: ProductsAdapterListener,
) : ListAdapter<Product, ProductsAdapter.ProductItemViewHolder>(
    ProductDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder =
        ProductItemViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener,
        )

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductItemViewHolder(
        private val binding: ItemProductBinding,
        private val listener: ProductsAdapterListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) = with(binding) {
            textViewTitle.text = product.name
            imageViewDiscount.isVisible = product.promo != null
            textViewPrice.text =
                root.resources.getString(R.string.price_label, product.price.formatToCurrency())
            mainLayout.setOnClickListener {
                listener.onProductClicked(product)
            }
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
        oldItem.code == newItem.code

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
        oldItem == newItem
}

interface ProductsAdapterListener {
    fun onProductClicked(product: Product)
}