package com.aman.swipeassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.File

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val product_id: Long = 0,
    @Json(name = "product_name")val name: String,
    @Json(name = "product_type")val type: String,
    val price: String,
    val tax: String,
    val image: File? ,
    var isSynced:Boolean = false
)