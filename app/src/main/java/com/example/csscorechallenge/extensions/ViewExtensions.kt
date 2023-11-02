package com.example.csscorechallenge.extensions

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.fadeIn(durationMillis: Long = 250) {
    if (visibility == View.GONE || visibility == View.INVISIBLE) {
        startAnimation(AlphaAnimation(0F, 1F).apply {
            duration = durationMillis
            fillAfter = true
            visible()
        })
    }
}

fun View.fadeOut(
    durationMillis: Long = 250,
    toInvisible: Boolean = false,
    onAnimationEnd: (() -> Unit?)? = null
) {
    if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
        startAnimation(AlphaAnimation(1F, 0F).apply {
            duration = durationMillis
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                }

                override fun onAnimationEnd(animation: Animation) {
                    if (toInvisible) {
                        invisible()
                    } else {
                        gone()
                    }
                    onAnimationEnd?.invoke()
                }

                override fun onAnimationRepeat(animation: Animation) {
                }
            })
        })
    }
}