package com.aman.swipeassignment

import android.app.Application
import com.aman.swipeassignment.api.ProductApi
import com.aman.swipeassignment.api.RetrofitBuilder
import com.aman.swipeassignment.repository.ProductsRepository

class ProductApp:Application() {

    lateinit var productRepo: ProductsRepository
    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        productRepo = ProductsRepository(RetrofitBuilder.getProductApi(), applicationContext )
    }

}