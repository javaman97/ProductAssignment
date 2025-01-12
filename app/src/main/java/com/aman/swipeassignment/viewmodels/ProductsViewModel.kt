package com.aman.swipeassignment.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.swipeassignment.data.models.PostApiResponse
import com.aman.swipeassignment.data.models.Product
import com.aman.swipeassignment.repository.ProductsRepository
import com.aman.swipeassignment.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel(private val repository: ProductsRepository):ViewModel() {

    private val _productListState = MutableLiveData<ResponseState<List<Product>>>()
    val productListState: LiveData<ResponseState<List<Product>>> get() = _productListState

    private val _isInternetConnected = MutableLiveData<Boolean>()
    val isInternetConnected: LiveData<Boolean> get() = _isInternetConnected

    private val _addProductState = MutableLiveData<ResponseState<PostApiResponse>>()
    val addProductState: LiveData<ResponseState<PostApiResponse>> get() = _addProductState


    fun updateConnectivityStatus(isConnected: Boolean) {
        _isInternetConnected.postValue(isConnected)
    }

    init {
        fetchProducts()
    }

        private fun fetchProducts() {
            viewModelScope.launch(Dispatchers.IO) {
                _productListState.postValue(ResponseState.Loading)
                try {
                    val products = repository.getProducts()
                    _productListState.postValue(ResponseState.Success(data = products))
                } catch (e: Exception) {
                    _productListState.postValue(ResponseState.Failure(e.message))
                }
            }
    }

    fun addProduct(
        productName: String,
        productType: String,
        price: Double,
        taxRate: Double
    ) {
        viewModelScope.launch {
            _addProductState.postValue(ResponseState.Loading)
            try {
                val response = repository.postProduct(productName, productType, price, taxRate)
                if (response.isSuccessful && response.body() != null) {
                    _addProductState.postValue(ResponseState.Success(response.body()!!))
                } else {
                    _addProductState.postValue(
                        ResponseState.Failure("Error: ${response.errorBody()?.string()}")
                    )
                }
            } catch (e: Exception) {
                _addProductState.postValue(ResponseState.Failure("Exception: ${e.message}"))
            }
        }
    }

}