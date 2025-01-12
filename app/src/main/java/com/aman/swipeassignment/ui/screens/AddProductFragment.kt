package com.aman.swipeassignment.ui.screens

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.aman.swipeassignment.R
import com.aman.swipeassignment.databinding.FragmentAddProductListDialogBinding
import com.aman.swipeassignment.utils.Constants.AddProductFragmentTAG
import com.aman.swipeassignment.utils.ResponseState
import com.aman.swipeassignment.utils.toast
import com.aman.swipeassignment.viewmodels.ProductsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AddProductFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddProductListDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductsViewModel by sharedViewModel()
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
                toast("Give MEDIA Permission")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentAddProductListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmit.setOnClickListener {
              submitProductsData()
        }

        binding.btnUploadImage.setOnClickListener {
            checkAndRequestPermissions()
        }
    }


    private fun submitProductsData() {
        val productName: String = binding.etxtProductName.text?.toString() ?: ""
        val productType: String = binding.spinnerProductType.isSelected.toString()
        val price: Double = binding.etxtSellingPrice.text?.toString()?.toDoubleOrNull() ?: 0.0
        val taxRate: Double = binding.etxtTaxRate.text?.toString()?.toDoubleOrNull() ?: 0.0

        if (productName.isEmpty() || productType.isEmpty() || price <= 0 || taxRate < 0) {
            toast("Fill all the product Details")
            return
        }

        Log.d(AddProductFragmentTAG, "Product Details: $productName, $productType, $price, $taxRate,")

        viewModel.addProductState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResponseState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d(AddProductFragmentTAG, "Loading Product")
                }
                is ResponseState.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d(AddProductFragmentTAG, "Adding Product FAILURE! ${state.error}")
                    showCustomDialog(false)
                    showProductNotification(requireContext(), false)
                }
                is ResponseState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d(AddProductFragmentTAG, "Product added Successfully: ${state.data}")

                    showCustomDialog(true)
                    showProductNotification(requireContext(), true)
                    dismiss()
                }
            }
        }


        viewModel.addProduct(productName, productType, price, taxRate)
    }


    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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

    private fun showProductNotification(context: Context, isSuccess: Boolean) {
        val channelId = "product_notification_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Product Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon( if (isSuccess) R.drawable.ic_done_24 else R.drawable.ic_failure)
            .setContentTitle(getString(if (isSuccess) R.string.success_title else R.string.failure_title))
            .setContentText(getString(if (isSuccess) R.string.success_msg else R.string.failure_msg))
            .setStyle(NotificationCompat.BigTextStyle().bigText("Product Status"))
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(0, notification)
    }


    private fun showCustomDialog(isSuccess: Boolean) {

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_product_actiondialog, null)
        val dialogTitle: TextView = dialogView.findViewById(R.id.dialog_title)
        val dialogMessage: TextView = dialogView.findViewById(R.id.dialog_message)
        val dialogButton: Button = dialogView.findViewById(R.id.dialog_ok_button)

        if (isSuccess) {
            dialogTitle.text = getString(R.string.success_title)
            dialogMessage.text = getString(R.string.success_msg)
            dialogButton.text = getString(R.string.okay)
            dialogButton.setBackgroundColor(Color.GREEN)
        } else {
            dialogTitle.text = getString(R.string.failure_title)
            dialogMessage.text = getString(R.string.failure_msg)
            dialogButton.text = getString(R.string.retry)
            dialogButton.setBackgroundColor(Color.RED)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialogButton.setOnClickListener {
            dialog.dismiss()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.addProductState.removeObservers(viewLifecycleOwner)
    }

}