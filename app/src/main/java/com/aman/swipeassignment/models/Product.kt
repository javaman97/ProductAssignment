package com.aman.swipeassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val image: String?,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)