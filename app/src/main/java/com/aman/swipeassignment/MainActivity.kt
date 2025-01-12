package com.aman.swipeassignment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aman.swipeassignment.databinding.ActivityMainBinding
import com.aman.swipeassignment.ui.screens.AddProductFragment
import com.aman.swipeassignment.utils.Constants.MainActivityTAG
import com.aman.swipeassignment.utils.NetworkUtils
import com.aman.swipeassignment.utils.setVisibility
import com.aman.swipeassignment.viewmodels.ProductsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private  val productsViewModel: ProductsViewModel by inject()
    private lateinit var  addProductFragment:AddProductFragment
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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as? NavHostFragment

        if (navHostFragment != null) {
            navController = navHostFragment.navController


            binding.fabAddProduct.setOnClickListener {
                addProductFragment = AddProductFragment()
                addProductFragment.show(supportFragmentManager, "AddProductDialogFragment")
            }
        } else {
            Log.e(MainActivityTAG, "NavHostFragment not found!")
        }

        productsViewModel.isInternetConnected.observe(this) { isInternetConnected ->
            if (isInternetConnected) {
                if(wasPreviouslyConnected==false){
                setInternetStatusUI(R.color.green, R.string.internet_connection_back)
                lifecycleScope.launch {
                    delay(3000)
                    binding.internetStatus.setVisibility(false)
                    window.statusBarColor = resources.getColor(android.R.color.transparent, theme)
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
                delay(5000)
            }
        }
    }

    private fun setInternetStatusUI(color: Int, statusText:Int){
        binding.internetStatus.setBackgroundColor(ContextCompat.getColor(this, color))
        binding.txtConnectionMsg.text = getString(statusText)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(color, theme)
        }
    }

}