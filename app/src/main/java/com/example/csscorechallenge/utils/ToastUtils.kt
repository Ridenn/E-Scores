package com.example.csscorechallenge.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {
    fun showToastMessage(toastMessage: String, context: Context) {
        Toast.makeText(
            context,
            toastMessage,
            Toast.LENGTH_LONG
        ).show()
    }
}