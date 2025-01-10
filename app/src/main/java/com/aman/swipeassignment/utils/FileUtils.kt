package com.aman.swipeassignment.utils

import android.content.Context
import android.net.Uri
import java.io.File

object FileUtils {
    fun uriToFile(context: Context, uri: Uri): File {
        val file = File(context.cacheDir, "image_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }
}
