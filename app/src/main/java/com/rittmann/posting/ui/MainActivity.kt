package com.rittmann.posting.ui

import android.os.Bundle
import com.rittmann.posting.R
import com.rittmann.posting.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
