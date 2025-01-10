package com.aman.swipeassignment.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.aman.swipeassignment.api.RetrofitBuilder
import com.aman.swipeassignment.databinding.FragmentAddProductListDialogBinding
import com.aman.swipeassignment.local.ProductDatabase
import com.aman.swipeassignment.repository.ProductsRepository
import com.aman.swipeassignment.utils.ResponseState
import com.aman.swipeassignment.utils.toast
import com.aman.swipeassignment.viewmodels.ProductsViewModel
import com.aman.swipeassignment.viewmodels.ProductsViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddProductFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddProductListDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProductsViewModel
    private val repository: ProductsRepository by lazy { ProductsRepository(RetrofitBuilder.getProductApi(),
        ProductDatabase.getProductDatabase(requireContext()), requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentAddProductListDialogBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ProductsViewModelFactory(repository))[ProductsViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmit.setOnClickListener {
            if(validateProductFields()){
                submitProductsData()
            }
        }
    }


    private fun submitProductsData() {
        val productName:String = binding.etxtProductName.text.toString().trim()
        val productType: String = binding.spinnerProductType.onItemSelectedListener.toString().trim()
        val price: Double = binding.etxtSellingPrice.text.toString().toDouble()
        val taxRate: Double = binding.etxtTaxRate.text.toString().toDouble()


        viewModel.addProductState.observe(viewLifecycleOwner){ state ->
            when(state){
                is ResponseState.Loading -> {

                }
                is ResponseState.Failure ->{
                    binding.progressBar.visibility = View.GONE
                    toast(state.error)
                }

                is ResponseState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    viewModel.addProduct(productName,productType,price,taxRate)
                }
            }

        }

    }

    private fun validateProductFields(): Boolean {
        if(!binding.spinnerProductType.isSelected){
            toast("Please select Product's type")
            return false
        }
        if(binding.etxtProductName.text.toString().trim().isEmpty()){
            toast("Please enter Product's Name")
            return false
        }

        try {
            val sellingPrice: Double = binding.etxtSellingPrice.text.toString().toDouble()
            val taxRate: Double = binding.etxtTaxRate.text.toString().toDouble()
        } catch (e:NumberFormatException){
            toast("Please enter Price or Tax(in decimals)")
            return false
        }
        return true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "Adding Product Screen"
    }
}