// File: app/src/main/java/com/papb/projectakhirandroid/utils/Utils.kt

package com.papb.projectakhirandroid.utils

import android.content.Context
import android.widget.Toast

object Utils {
    /**
     * Menampilkan pesan Toast singkat.
     * Dapat dipanggil dari Composable menggunakan LocalContext.current
     */
    fun displayToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}