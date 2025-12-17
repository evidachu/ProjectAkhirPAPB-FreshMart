package com.papb.projectakhirandroid.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object ImageUtils {

    /**
     * Reads a Uri, resizes it, compresses it, and saves it to a temporary file.
     * Returns the File object or null if failed.
     * Runs on Dispatchers.IO to prevent blocking the Main Thread.
     */
    suspend fun uriToTempFile(context: Context, uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            try {
                val contentResolver = context.contentResolver

                // 1. Decode bounds only to get dimensions (Lightweight)
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                var inputStream = contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream, null, options)
                inputStream?.close()

                // 2. Calculate inSampleSize to resize image (Target ~1024px max)
                options.inSampleSize = calculateInSampleSize(options, 1024, 1024)
                options.inJustDecodeBounds = false

                // 3. Decode full image with scaling (Memory safe)
                inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                inputStream?.close()

                if (bitmap == null) return@withContext null

                // 4. Create a temporary file in the cache directory
                val cacheDir = context.cacheDir
                val tempFile = File.createTempFile("upload_", ".jpg", cacheDir)
                val outputStream = FileOutputStream(tempFile)

                // 5. Compress to JPEG (Quality 60 is usually good enough for web)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
                outputStream.flush()
                outputStream.close()

                // 6. Recycle bitmap to free memory immediately
                bitmap.recycle()

                tempFile
            } catch (t: Throwable) {
                // Catch OutOfMemoryError and other exceptions
                t.printStackTrace()
                null
            }
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
