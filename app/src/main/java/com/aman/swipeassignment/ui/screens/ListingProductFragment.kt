package com.aman.swipeassignment.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.swipeassignment.api.RetrofitBuilder
import com.aman.swipeassignment.databinding.FragmentListingProductBinding
import com.aman.swipeassignment.local.ProductDatabase
import com.aman.swipeassignment.repository.ProductsRepository
import com.aman.swipeassignment.ui.adapter.ListingProductAdapter
import com.aman.swipeassignment.utils.Constants.ListingProductFragmentTAG
import com.aman.swipeassignment.utils.ResponseState
import com.aman.swipeassignment.utils.toast
import com.aman.swipeassignment.viewmodels.ProductsViewModel
import com.aman.swipeassignment.viewmodels.ProductsViewModelFactory
import java.util.Locale


class ListingProductFragment : Fragment() {

    private var _binding: FragmentListingProductBinding? = null

    private val binding get() = _binding!!
    private lateinit var productAdapter: ListingProductAdapter
    private lateinit var viewModel: ProductsViewModel
    private val repository: ProductsRepository by lazy { ProductsRepository(RetrofitBuilder.getProductApi(),
        ProductDatabase.getProductDatabase(requireContext()), requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentListingProductBinding.inflate(inflater, container, false)
         viewModel = ViewModelProvider(this, ProductsViewModelFactory(repository))[ProductsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        onSearchProducts()
    }

    private fun onSearchProducts() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(ListingProductFragmentTAG, "Searched Query $query")
              return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(ListingProductFragmentTAG, "Query Changed $newText")
                filterProductList(newText.orEmpty())
                return true
            }

        })
    }

    private fun filterProductList(searchQuery: String?) {

        val listState = viewModel.productListState.value
        if(searchQuery!=null && listState is ResponseState.Success){
            val searchProductList = listState.data
            val filterList = searchProductList.filter { product ->

                product.product_name.lowercase(Locale.ROOT).contains(searchQuery.lowercase(Locale.ROOT)) ||
                        product.product_type.lowercase(Locale.ROOT).contains(searchQuery.lowercase(Locale.ROOT)) ||
                        product.price.toString().contains(searchQuery) || product.tax.toString().contains(searchQuery)
            }

            if(filterList.isEmpty()){
                Toast.makeText(requireContext(), "No Products Found", Toast.LENGTH_SHORT).show()
            } else{
                productAdapter.updateProductList(filterList.toMutableList())
            }
        } else{
            productAdapter.updateProductList(mutableListOf())
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            productAdapter = ListingProductAdapter()
            adapter = productAdapter
        }
    }

    private fun observeViewModel(){
        viewModel.productListState.observe(viewLifecycleOwner){ state ->
            when(state){
                is ResponseState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResponseState.Failure ->{
                    binding.progressBar.visibility = View.GONE
                    toast("Fetching Products FAILED!")
                }

                is ResponseState.Success -> {
                    binding.progressBar.visibility = View.GONE

                    productAdapter.updateProductList(state.data)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}