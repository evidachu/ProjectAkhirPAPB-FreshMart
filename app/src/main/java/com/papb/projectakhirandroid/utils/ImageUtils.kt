package com.papb.projectakhirandroid.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

object ImageUtils {

    fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
        return try {
            val contentResolver = context.contentResolver
            
            // 1. Decode bounds only to get dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            // 2. Calculate inSampleSize to resize image
            options.inSampleSize = calculateInSampleSize(options, 1024, 1024) // Max 1024x1024
            options.inJustDecodeBounds = false

            // 3. Decode full image with scaling
            inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            if (bitmap == null) return null

            // 4. Compress to JPEG
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream) // Quality 70%
            val byteArray = outputStream.toByteArray()
            
            bitmap.recycle()
            
            byteArray
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
