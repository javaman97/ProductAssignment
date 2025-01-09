package com.aman.swipeassignment.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aman.swipeassignment.models.ProductEntity

@Dao
interface ProductDao {
    @Insert
    suspend fun addProduct(product: ProductEntity)

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductEntity

}