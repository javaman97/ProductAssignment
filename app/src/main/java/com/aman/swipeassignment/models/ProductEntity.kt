package com.aman.swipeassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @Json(name = "product_name")val name: String,
    @Json(name = "product_type")val type: String,
    val price: String,
    val tax: String,
    val images: String? = null
)