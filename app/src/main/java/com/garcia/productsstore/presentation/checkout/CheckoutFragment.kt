package com.garcia.productsstore.presentation.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.garcia.productsstore.R
import com.garcia.productsstore.common.formatToCurrency
import com.garcia.productsstore.databinding.FragmentCheckoutBinding
import com.garcia.productsstore.presentation.checkout.adapter.CartAdapter
import com.garcia.productsstore.presentation.checkout.adapter.CartAdapterListener
import com.garcia.productsstore.presentation.utils.AlertDialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : Fragment(), CartAdapterListener {
    private val viewModel: CheckoutViewModel by viewModels()
    private lateinit var binding: FragmentCheckoutBinding
    private val adapter = CartAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        initObservers()
    }

    private fun setupUi() {
        binding.apply {
            buttonGoToCheckout.setOnClickListener {
                findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToPayFragment())
            }
            rvCart.adapter = adapter
        }
    }

    private fun initObservers() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { viewState ->
            viewState.cart?.let {
                binding.rvCart.isVisible = true
                adapter.submitList(it.items)
                binding.textViewTotalValue.text =
                    getString(R.string.price_label, it.total.formatToCurrency())
            }

            if (viewState.emptyCart) {
                showEmptyCartAlertDialog()
            }
        }
    }

    override fun onRemoveClicked(productCode: String) {
        showRemoveItemAlertDialog(productCode)
    }

    private fun showRemoveItemAlertDialog(productCode: String) {
        AlertDialogs.showOkCancelAlertDialog(
            context = requireContext(),
            title = getString(R.string.are_you_sure),
            message = getString(R.string.remove_item_warning),
            onOkClicked = {
                viewModel.onRemoveItemPressed(productCode)
            },
            onCancelClicked = null,
        )
    }

    private fun showEmptyCartAlertDialog() {
        val alert = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.empty_cart))
            .setMessage(R.string.empty_cart_description)
            .setPositiveButton(R.string.ok) { _, _ -> findNavController().popBackStack() }
            .setCancelable(false)
            .create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}