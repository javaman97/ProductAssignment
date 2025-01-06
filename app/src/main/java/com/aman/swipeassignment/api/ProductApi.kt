package com.aman.swipeassignment.api


import com.aman.swipeassignment.models.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductApi {

    @GET("get")
    suspend fun getAllProducts(): Response<List<Product>>

}