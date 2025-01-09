package com.aman.swipeassignment.api


import com.aman.swipeassignment.models.Product
import com.aman.swipeassignment.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface ProductApi {

    @GET(Constants.GET)
    suspend fun getAllProducts(): Response<List<Product>>

}