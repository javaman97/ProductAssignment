package com.aman.swipeassignment.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aman.swipeassignment.models.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductDatabase:RoomDatabase() {
    abstract fun productDao():ProductDao

    companion object{
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getProductDatabase(context: Context):ProductDatabase{
            return INSTANCE ?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "product_db"
                ).build()

                INSTANCE = instance
                instance
            }

        }

    }

}