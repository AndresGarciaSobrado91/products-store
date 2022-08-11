package com.garcia.productsstore.common

import com.garcia.productsstore.R

data class Error(
    val message: String? = null,
    val resourceId: Int = R.string.generic_error,
)