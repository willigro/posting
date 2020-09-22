package com.rittmann.posting.internal

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireContext(), msg, duration).show()
}

fun AppCompatActivity.toast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, msg, duration).show()
}