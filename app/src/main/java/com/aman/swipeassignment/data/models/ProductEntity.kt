package com.aman.swipeassignment.data.models
import com.squareup.moshi.Json



data class ProductEntity(
    @Json(name = "product_name") val name: String,
    @Json(name = "product_type") val type: String,
    val price: String,
    val tax: String,
)