package com.aman.swipeassignment.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.swipeassignment.api.ProductApi
import com.aman.swipeassignment.api.RetrofitBuilder
import com.aman.swipeassignment.ui.adapter.ListingProductAdapter
import com.aman.swipeassignment.databinding.FragmentListingProductBinding
import com.aman.swipeassignment.repository.ProductsRepository
import com.aman.swipeassignment.utils.ResponseState
import com.aman.swipeassignment.viewmodels.ProductsViewModel
import com.aman.swipeassignment.viewmodels.ProductsViewModelFactory


class ListingProductFragment : Fragment() {

    private var _binding: FragmentListingProductBinding? = null

    private val binding get() = _binding!!
    private lateinit var productAdapter: ListingProductAdapter
    private lateinit var viewModel: ProductsViewModel
    private val repository: ProductsRepository by lazy { ProductsRepository(RetrofitBuilder.getProductApi(), requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentListingProductBinding.inflate(inflater, container, false)
         viewModel = ViewModelProvider(this, ProductsViewModelFactory(repository)).get(ProductsViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
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