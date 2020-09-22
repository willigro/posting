package com.rittmann.posting.internal

import android.view.View
import android.widget.EditText
import com.rittmann.posting.R

const val validResource = R.drawable.background_input_gray
const val invalidResource = R.drawable.background_input_red

fun EditText?.isValid(isValid: Boolean?, errorResIs: Int, invalidText: View? = null) {
    this?.apply {
        when (isValid) {
            true -> {
                setBackgroundResource(validResource)
                invalidText?.visibility = View.GONE
            }
            else -> {
                setBackgroundResource(invalidResource)
                invalidText?.visibility = View.VISIBLE
                error = context.getString(errorResIs)
            }
        }
    }
}