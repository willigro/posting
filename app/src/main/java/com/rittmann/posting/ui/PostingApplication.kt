package com.rittmann.posting.ui

import android.app.Application
import com.rittmann.androidtools.troubles.CrashUtils
import com.rittmann.posting.R
import com.rittmann.widgets.dialog.DialogUtil

class PostingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DialogUtil.defaultTitle = getString(R.string.app_name)

        CrashUtils.init()
    }
}