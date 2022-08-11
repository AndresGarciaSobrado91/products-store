package com.garcia.productsstore.presentation.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.garcia.productsstore.R

object AlertDialogs {

    fun showOkCancelAlertDialog(
        context: Context,
        title: String,
        message: String,
        onOkClicked: (() -> Unit)?,
        onCancelClicked: (() -> Unit)?,
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.ok) { _, _ -> onOkClicked?.invoke() }
        builder.setNegativeButton(R.string.cancel) { _, _ -> onCancelClicked?.invoke() }
        builder.setOnDismissListener { onCancelClicked?.invoke() }
        builder.show()
    }
}