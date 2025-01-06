package com.aman.swipeassignment.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.swipeassignment.models.Product
import com.aman.swipeassignment.repository.ProductsRepository
import com.aman.swipeassignment.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel(private val repository: ProductsRepository):ViewModel() {

    private val _productListState = MutableLiveData<ResponseState<List<Product>>>()
    val productListState: LiveData<ResponseState<List<Product>>> get() = _productListState

    init {
        fetchProducts()
    }

        private fun fetchProducts() {
            viewModelScope.launch {
                _productListState.postValue(ResponseState.Loading)
                try {
                    val products = repository.getProducts()
                    _productListState.postValue(ResponseState.Success(products))
                } catch (e: Exception) {
                    _productListState.postValue(ResponseState.Failure(e.message))
                }
            }
    }

}