package com.aman.swipeassignment.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aman.swipeassignment.R
import com.aman.swipeassignment.api.RetrofitBuilder
import com.aman.swipeassignment.databinding.FragmentAddProductListDialogBinding
import com.aman.swipeassignment.local.ProductDatabase
import com.aman.swipeassignment.models.Product
import com.aman.swipeassignment.repository.ProductsRepository
import com.aman.swipeassignment.utils.Constants.AddProductFragmentTAG
import com.aman.swipeassignment.utils.FileUtils
import com.aman.swipeassignment.utils.ResponseState
import com.aman.swipeassignment.utils.toast
import com.aman.swipeassignment.viewmodels.ProductsViewModel
import com.aman.swipeassignment.viewmodels.ProductsViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


class AddProductFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddProductListDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProductsViewModel
    private val repository: ProductsRepository by lazy { ProductsRepository(RetrofitBuilder.getProductApi(),
        ProductDatabase.getProductDatabase(requireContext()), requireContext()) }
    private var imageUri: Uri? = null

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                if (imageUri != null) {
                    binding.btnUploadImage.setImageURI(imageUri)
                    Log.d(AddProductFragmentTAG, "Image selected: $imageUri")
                } else {
                    Log.d(AddProductFragmentTAG, "No image selected")
                }
            }
        }


    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openImagePicker()
            } else {
                toast("Give Permission for Images and Media")
            }
        }

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

        binding.btnUploadImage.setOnClickListener {
            checkAndRequestPermissions()
        }
    }


    private fun submitProductsData() {
        val productName:String = binding.etxtProductName.text?.toString() ?:""
        val productType: String = binding.spinnerProductType.isSelected.toString()
        val price: Double = binding.etxtSellingPrice.text.toString().toDouble()
        val taxRate: Double = binding.etxtTaxRate.text.toString().toDouble()
        val imageFile = imageUri?.let { FileUtils.uriToFile(requireContext(), it) }
        val result = RetrofitBuilder.getProductApi()

        Log.d(AddProductFragmentTAG,"SubmitProductsData Called")

        viewModel.addProductState.observe(viewLifecycleOwner){ state ->
            when(state){
                is ResponseState.Loading -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d(AddProductFragmentTAG,"Loading Product")
                }
                is ResponseState.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d(AddProductFragmentTAG,"Loading Product FAILURE! ${state.error}")
                }

                is ResponseState.Success -> {
                    binding.progressBar.visibility = View.GONE

                    lifecycleScope.launch {
                        try {
                            val response = result.getResponse(productName, productType, price, taxRate, imageFile)
                            Log.d("Response from URL", response.toString())

                            if (response.isSuccessful && response.body() != null)
                                toast("Data Submitted Successfully")
                             else {
                                Log.d("Response from URL", "Failed to submit product. Error: ${response.errorBody()?.string()}")
                                toast("Product Added FAILED!")
                            }
                        } catch (e: Exception) {
                            Log.d("Response from URL", "Exception: ${e.message}")
                        }
                    }
                }
            }

        }

    }

    private fun validateProductFields(): Boolean {
        if(binding.spinnerProductType.text.isNullOrEmpty() ||
            binding.spinnerProductType.text.toString() !in resources.getStringArray(R.array.product_list)){
            toast("Please select Product's type")
            return false
        }
        else if(binding.etxtProductName.text.toString().trim().isEmpty()){
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

    private fun checkAndRequestPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }


    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}