package com.garcia.productsstore.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.garcia.productsstore.data.local.CartDao
import com.garcia.productsstore.domain.model.CartItem

@Database(entities = [CartItem::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao
}