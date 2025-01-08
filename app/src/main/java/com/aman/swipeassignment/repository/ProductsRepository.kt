package com.aman.swipeassignment.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aman.swipeassignment.api.ProductApi
import com.aman.swipeassignment.models.Product

class ProductsRepository(private val productApi:ProductApi, private val applicationContext:Context) {
    private val productsList = MutableLiveData<List<Product>>()

     val product: LiveData<List<Product>>
         get() = productsList

    suspend fun getProducts():List<Product>
    {
        return try {
             val result = productApi.getAllProducts()

                if (result.isSuccessful){
                    result.body() ?: emptyList()
                }
             else{
                emptyList()
            }
            } catch (e:Exception){
                Log.e("Get Products Error", " ${e.message}")
                emptyList()
        }
    }
}