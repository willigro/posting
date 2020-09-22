package com.rittmann.posting.ui.base

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rittmann.posting.R

abstract class BaseActivity : AppCompatActivity() {

    /* Duplicated - BaseFragment */
    fun menuClick(execute: (view: View) -> Unit) {
        findViewById<View>(R.id.iconMoreToolbar).apply {
            visibility = View.VISIBLE
            setOnClickListener {
                execute(it)
            }
        }
    }

    /* Duplicated - BaseFragment */
    fun setToolbar(title: String, hideButton: Boolean = false) {
        findViewById<View>(R.id.backButtonImage).apply {
            visibility = if (hideButton) View.INVISIBLE else View.VISIBLE
            setOnClickListener {
                onBackPressed()
            }
        }
        findViewById<TextView>(R.id.titleOfBackButton).text = title
    }
}