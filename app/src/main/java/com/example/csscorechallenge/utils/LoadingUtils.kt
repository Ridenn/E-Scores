package com.example.csscorechallenge.utils

import android.content.Context
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.csscorechallenge.R

object LoadingUtils {
    fun showloadingProgressBar(context: Context) = CircularProgressDrawable(context).apply {
            centerRadius = 30f
            strokeWidth = 5f
            setColorSchemeColors(R.color.white, R.color.white, R.color.white)
        }

    fun getErrorPlayer() =
        when ((1..5).random()) {
            1 -> {
                R.drawable.ic_error_player_1
            }
            2 -> {
                R.drawable.ic_error_player_2
            }
            3 -> {
                R.drawable.ic_error_player_3
            }
            4 -> {
                R.drawable.ic_error_player_4
            }
            else -> {
                R.drawable.ic_error_player_5
            }
        }
}