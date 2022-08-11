package com.garcia.productsstore.presentation.addItemToCart

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.garcia.productsstore.R
import com.garcia.productsstore.databinding.BottomSheetAddItemToCartBinding
import com.garcia.productsstore.presentation.utils.AlertDialogs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddItemToCartBottomSheet : BottomSheetDialogFragment() {
    private val viewModel: AddItemToCartViewModel by viewModels()
    private lateinit var binding: BottomSheetAddItemToCartBinding
    private val args: AddItemToCartBottomSheetArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setProduct(args.product)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddItemToCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        initObservers()
    }

    private fun setupUI() {
        with(binding) {
            buttonAdd.setOnClickListener {
                viewModel.onAddPressed()
            }
            buttonSubtract.setOnClickListener {
                viewModel.onSubtractPressed()
            }
            buttonAddToCart.setOnClickListener {
                viewModel.onAddToCartPressed()
                dismiss()
            }
        }
    }

    private fun initObservers() {
        with(binding) {
            viewModel.stateLiveData.observe(viewLifecycleOwner) { viewState ->
                buttonAddToCart.text = when (viewState.productAlreadyInCart) {
                    true -> getString(R.string.update_cart)
                    false -> getString(R.string.add_to_cart)
                }

                viewState.cartItem?.let {
                    textViewTitle.text = it.productName
                    textViewPromo.text = viewState.promoDescription
                    textViewPromo.isVisible = viewState.promoDescription.isNullOrBlank().not()
                    textViewPriceValue.text =
                        getString(R.string.price_label, it.productPrice.toString())
                    textViewQuantity.text = it.quantity.toString()
                    textViewTotalValue.text = getString(R.string.price_label, it.total.toString())
                    textViewGlobalPrice.isVisible = it.discountApplied
                    textViewGlobalPrice.text = Html.fromHtml(
                        "<del>${
                            getString(
                                R.string.price_label,
                                it.globalPrice.toString()
                            )
                        }</del>",
                        Html.FROM_HTML_MODE_COMPACT
                    )

                    buttonSubtract.setIconResource(
                        if (it.quantity == 1) {
                            R.drawable.ic_trash
                        } else {
                            R.drawable.ic_subtract
                        }
                    )
                }

                if (viewState.showRemoveItemAlert) {
                    AlertDialogs.showOkCancelAlertDialog(
                        context = requireContext(),
                        title = getString(R.string.are_you_sure),
                        message = getString(R.string.remove_item_warning),
                        onOkClicked = {
                            viewModel.onRemoveItemConfirmation(true)
                            findNavController().popBackStack()
                        },
                        onCancelClicked = { viewModel.onRemoveItemConfirmation(false) },
                    )
                }
            }
        }
    }
}