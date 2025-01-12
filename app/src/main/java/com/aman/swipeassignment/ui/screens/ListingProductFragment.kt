package com.aman.swipeassignment.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.swipeassignment.databinding.FragmentListingProductBinding
import com.aman.swipeassignment.ui.adapter.ListingProductAdapter
import com.aman.swipeassignment.utils.Constants.ListingProductFragmentTAG
import com.aman.swipeassignment.utils.ResponseState
import com.aman.swipeassignment.utils.toast
import com.aman.swipeassignment.viewmodels.ProductsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Locale


class ListingProductFragment : Fragment() {

    private var _binding: FragmentListingProductBinding? = null

    private val binding get() = _binding!!
    private lateinit var productAdapter: ListingProductAdapter
    private val viewModel: ProductsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentListingProductBinding.inflate(inflater, container, false)
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
                    Log.d(ListingProductFragmentTAG, "Loading Fetched Products")
                }
                is ResponseState.Failure ->{
                    binding.progressBar.visibility = View.GONE
                    toast("Fetching Products FAILED!")
                    Log.d(ListingProductFragmentTAG, " Fetched Products FAILED!  ${state.error}")
                }

                is ResponseState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    productAdapter.updateProductList(state.data)
                    Log.d(ListingProductFragmentTAG, "Products Fetched Successfully ${state.data}")
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.addProductState.removeObservers(viewLifecycleOwner)
    }

}