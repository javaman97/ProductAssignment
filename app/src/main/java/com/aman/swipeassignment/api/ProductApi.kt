package com.aman.swipeassignment.api


import com.aman.swipeassignment.models.PostApiResponse
import com.aman.swipeassignment.models.Product
import com.aman.swipeassignment.models.ProductEntity
import com.aman.swipeassignment.utils.Constants
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApi {

    @GET(Constants.GET)
    suspend fun getAllProducts(): List<Product>

    @FormUrlEncoded
    @POST(Constants.ADD)
    suspend fun getResponse(
        @Field("product_name") product_name:String,
        @Field("product_type") product_type:String,
        @Field("price") price: Double,
        @Field("tax") tax:Double

    ): Response<PostApiResponse>

}