package com.aman.swipeassignment.local

import androidx.room.TypeConverter
import java.io.File

class Converters {

    @TypeConverter
    fun fromFileToString(file: File?): String? {
        return file?.absolutePath
    }

    @TypeConverter
    fun fromStringToFile(path: String?): File? {
        return if (path.isNullOrEmpty()) null else File(path)
    }
}