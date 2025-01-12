package com.aman.swipeassignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aman.swipeassignment.R
import com.aman.swipeassignment.data.models.Product
import com.aman.swipeassignment.databinding.ListingItemBinding

class ListingProductAdapter :RecyclerView.Adapter<ListingProductAdapter.ListingProductViewHolder>() {
   private val productList: MutableList<Product> = mutableListOf()

    inner class ListingProductViewHolder(private val binding: ListingItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product){
            binding.txtProductName.text = product.product_name
            binding.txtProductType.text = product.product_type
            binding.txtPrice.text = "$ ${"%.1f".format(product.price)}"
            binding.txtTax.text = "Tax at ${"%.1f".format(product.tax)}%"

            binding.imgProduct.load(product.image){
                crossfade(true)
                placeholder(R.drawable.preloader)
                error(R.drawable.product_not_found)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingProductViewHolder {

        val binding = ListingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListingProductViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ListingProductViewHolder, position: Int) {
        val item = productList[position]
        holder.bind(item)
    }

    fun updateProductList(product: List<Product>){
        productList.clear()
        productList.addAll(product)
        notifyDataSetChanged()
    }

}