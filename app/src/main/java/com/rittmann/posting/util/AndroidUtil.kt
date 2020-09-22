package com.rittmann.posting.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object AndroidUtil {

    private fun getInputMethodManager(context: Context) =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    fun hideKeyboard(context: Context, view: View) {
        val imm = getInputMethodManager(context)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}