package com.rittmann.widgets.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity

object AndroidUtil {

    fun getPercentHeight(context: Context, percent: Float): Int {
        val displayMetrics = DisplayMetrics()
        (context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return (displayMetrics.heightPixels * percent / 100).toInt()
    }

    fun openLinkIntoBrowse(context: Context, url: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            context.startActivity(this)
        }
    }

    fun convertToDP(context: Context, dp: Float): Int {
        return convertToPX(dp, TypedValue.COMPLEX_UNIT_DIP, context)
    }

    fun convertToPX(value: Float, scale: Int, context: Context): Int {
        return TypedValue.applyDimension(
            scale, value, context
                .resources.displayMetrics
        ).toInt()
    }
}