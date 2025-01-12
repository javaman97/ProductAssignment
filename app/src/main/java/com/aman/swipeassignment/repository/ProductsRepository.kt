package com.aman.swipeassignment.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aman.swipeassignment.data.api.ProductApi
import com.aman.swipeassignment.data.models.PostApiResponse
import com.aman.swipeassignment.data.models.Product
import com.aman.swipeassignment.data.models.ProductEntity
import com.aman.swipeassignment.utils.NetworkUtils
import retrofit2.Response

class ProductsRepository(private val productApi: ProductApi, private val applicationContext:Context) {
    private val productsList = MutableLiveData<List<ProductEntity>>()
    val product: LiveData<List<ProductEntity>>
        get() = productsList

    suspend fun getProducts(): List<Product> {
        return if (NetworkUtils.isConnected(applicationContext)) {
            productApi.getAllProducts()
        } else {
            emptyList()
        }
    }

    suspend fun postProduct(
        productName: String,
        productType: String,
        price: Double,
        tax: Double
    ): Response<PostApiResponse> {

        return productApi.getResponse(
            product_name = productName,
            product_type = productType,
            price = price,
            tax = tax
        )
    }

}

