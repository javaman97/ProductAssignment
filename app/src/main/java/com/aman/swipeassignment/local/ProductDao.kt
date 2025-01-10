package com.aman.swipeassignment.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aman.swipeassignment.models.Product
import com.aman.swipeassignment.models.ProductEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(products:List<ProductEntity>)

    @Query("SELECT * FROM products WHERE isSynced = 0")
    suspend fun getUnsyncedProducts(): List<ProductEntity>

    @Update
    suspend fun updateProduct(product: ProductEntity)

}