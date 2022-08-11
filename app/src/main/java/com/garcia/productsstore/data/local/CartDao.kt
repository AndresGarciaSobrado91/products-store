package com.garcia.productsstore.data.local

import androidx.room.*
import com.garcia.productsstore.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    fun getAllAsFlow(): Flow<List<CartItem>>

    @Query("SELECT * FROM cart WHERE product_code = :productCode")
    suspend fun getItem(productCode: String): CartItem?

    @Query("SELECT SUM(total) as total FROM cart")
    fun getCartTotalAsFlow(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)

    @Delete
    suspend fun delete(cartItem: CartItem)

    @Query("DELETE FROM cart WHERE product_code = :productCode")
    suspend fun delete(productCode: String)
}