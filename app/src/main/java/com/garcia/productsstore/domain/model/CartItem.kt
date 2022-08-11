package com.garcia.productsstore.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey @ColumnInfo(name = "product_code") val productCode: String,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "product_price") val productPrice: Double,
    var quantity: Int,
    @ColumnInfo(name = "global_price") var globalPrice: Double,
    @ColumnInfo(name = "discount_applied") var discountApplied: Boolean,
    var total: Double,
)