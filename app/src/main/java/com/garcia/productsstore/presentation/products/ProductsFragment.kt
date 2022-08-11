package com.garcia.productsstore.presentation.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.garcia.productsstore.R
import com.garcia.productsstore.databinding.FragmentProductsBinding
import com.garcia.productsstore.domain.model.Product
import com.garcia.productsstore.presentation.products.adapter.ProductsAdapter
import com.garcia.productsstore.presentation.products.adapter.ProductsAdapterListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(), ProductsAdapterListener {
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var binding: FragmentProductsBinding
    private val adapter = ProductsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        initObservers()
    }

    private fun setupUi() {
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener { viewModel.onSwipeToRefresh() }
            rvMain.setHasFixedSize(true)
            val staggeredGridLayoutManager =
                StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            rvMain.layoutManager = staggeredGridLayoutManager
            rvMain.adapter = adapter
            buttonGoToCheckout.setOnClickListener {
                findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToCheckoutFragment())
            }
        }
    }

    private fun initObservers() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { viewState ->
            binding.apply {
                swipeRefreshLayout.isRefreshing = viewState.isLoading
                textViewEmptyList.isVisible =
                    viewState.products.isEmpty() && viewState.isLoading.not()
                layoutCartDetails.isVisible =
                    viewState.products.isNotEmpty() && viewState.cartTotal != null

                if (viewState.products.isNotEmpty()) {
                    rvMain.isVisible = true
                    adapter.submitList(viewState.products)
                } else {
                    layoutCartDetails.isVisible = false
                }

                viewState.error?.let {
                    rvMain.isVisible = false
                    showAlertDialog(it.resourceId)
                }

                viewState.cartTotal?.let {
                    textViewTotalValue.text = getString(R.string.price_label, it)
                }
            }
        }
    }

    private fun showAlertDialog(resourceId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.error))
        builder.setMessage(resourceId)
        builder.setPositiveButton(R.string.ok) { _, _ -> }
        builder.show()
    }

    override fun onProductClicked(product: Product) {
        findNavController().navigate(
            ProductsFragmentDirections.actionProductsFragmentToAddItemToCartBottomSheet(
                product
            )
        )
    }
}