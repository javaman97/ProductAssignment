package com.aman.swipeassignment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aman.swipeassignment.api.ProductApi
import com.aman.swipeassignment.api.RetrofitBuilder
import com.aman.swipeassignment.databinding.ActivityMainBinding
import com.aman.swipeassignment.local.ProductDatabase
import com.aman.swipeassignment.repository.ProductsRepository
import com.aman.swipeassignment.ui.screens.AddProductFragment
import com.aman.swipeassignment.utils.NetworkUtils
import com.aman.swipeassignment.utils.setVisibility
import com.aman.swipeassignment.utils.toast
import com.aman.swipeassignment.viewmodels.ProductsViewModel
import com.aman.swipeassignment.viewmodels.ProductsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var  addProductFragment:AddProductFragment
    private lateinit var productRepo: ProductsRepository
    private lateinit var productApi:ProductApi
    private lateinit var productDB:ProductDatabase
    private var wasPreviouslyConnected: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        productApi = RetrofitBuilder.getProductApi()
        productDB = ProductDatabase.getProductDatabase(this)
        productRepo = ProductsRepository(productApi,productDB,applicationContext)

        productsViewModel= ViewModelProvider(this, ProductsViewModelFactory(productRepo))[ProductsViewModel::class.java]


        binding.reloadProducts.setOnClickListener {
            toast("Work in Progress or Restart App to See Changes")
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as? NavHostFragment

        if (navHostFragment != null) {
            navController = navHostFragment.navController


            binding.fabAddProduct.setOnClickListener {
                addProductFragment = AddProductFragment()
                addProductFragment.show(supportFragmentManager, "AddProductDialogFragment")
            }
        } else {
            Log.e(TAG, "NavHostFragment not found!")
        }

        productsViewModel.isInternetConnected.observe(this) { isInternetConnected ->
            if (isInternetConnected) {
                if(wasPreviouslyConnected==false){
                setInternetStatusUI(R.color.blue, R.string.internet_connection_back)
                lifecycleScope.launch {
                    delay(3000)
                    binding.internetStatus.setVisibility(false)
                }
            }
            } else {
                setInternetStatusUI(R.color.red, R.string.no_internet_connection)
            }
            wasPreviouslyConnected = isInternetConnected
        }

        monitorConnectivity()
    }

    private fun monitorConnectivity() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                val isConnected = NetworkUtils.isConnected(this@MainActivity)
                productsViewModel.updateConnectivityStatus(isConnected)
                productsViewModel.syncAddedProducts()
                delay(5000)
            }
        }
    }

    private fun setInternetStatusUI(color: Int, statusText:Int){
        binding.internetStatus.setBackgroundColor(ContextCompat.getColor(this, color))
        binding.txtConnectionMsg.text = getString(statusText)
    }

    companion object{
        const val TAG = "MainActivity"
    }

}