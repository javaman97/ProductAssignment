package com.aman.swipeassignment.data.models

data class PostApiResponse(
    val message: String,
    val product_details: ProductEntity,
    val product_id: Int,
    val success: Boolean
)
