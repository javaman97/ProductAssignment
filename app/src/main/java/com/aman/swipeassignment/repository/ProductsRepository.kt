package com.aman.swipeassignment.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aman.swipeassignment.api.ProductApi
import com.aman.swipeassignment.local.ProductDatabase
import com.aman.swipeassignment.models.Product
import com.aman.swipeassignment.models.ProductEntity
import com.aman.swipeassignment.utils.NetworkUtils

class ProductsRepository(private val productApi:ProductApi,val productDB:ProductDatabase, private val applicationContext:Context) {
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
    ): Boolean {
        val productEntity = ProductEntity(
            name = productName,
            type = productType,
            price = price.toString(),
            tax = tax.toString(),
        )

        return if (NetworkUtils.isConnected(applicationContext)) {
            try {
                val response = productApi.getResponse(productName, productType, price, tax)
                if (response.isSuccessful && response.body() != null) {
                    productDB.productDao().addProduct(listOf(productEntity)) // Save to Room
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        } else {
            productDB.productDao().addProduct(listOf(productEntity)) // Save locally for later sync
            false
        }

    }


    suspend fun syncOfflineProducts() {
        if (NetworkUtils.isConnected(applicationContext)) {
            val offlineProducts =
                productDB.productDao().getUnsyncedProducts() // Fetch unsynced products
            offlineProducts.forEach { product ->
                try {
                    val response = productApi.getResponse(
                        product.name,
                        product.type,
                        product.price.toDouble(),
                        product.tax.toDouble()
                    )
                    if (response.isSuccessful) {
                        product.isSynced = true
                        productDB.productDao().updateProduct(product) // Mark as synced
                    }
                } catch (e: Exception) {
                    Log.d("Product Repository","${e.message}")
                }
            }
        }

    }
}

