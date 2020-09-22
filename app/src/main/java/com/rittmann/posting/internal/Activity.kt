package com.rittmann.posting.internal

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.rittmann.posting.R
import com.rittmann.posting.ui.base.BaseActivity
import com.rittmann.posting.ui.base.BaseViewModel
import com.rittmann.posting.ui.base.LoadingScreenFragment
import com.rittmann.posting.util.ConstantsUtil.BACK_STACK

private val loadingList = arrayListOf<Fragment>()
private fun containsLoading() = loadingList.isNotEmpty()

fun BaseActivity.progress(viewModel: BaseViewModel) {
    viewModel.progress.observe(this, Observer { isLoading ->
        if (isLoading!!) {
            Log.i("testando", "show progress")
            findViewById<View>(R.id.progress).visibility = View.VISIBLE
        } else {
            Log.i("testando", "hide progress")
            findViewById<View>(R.id.progress).visibility = View.GONE
        }
    })
}

fun BaseActivity.dispatchFragment(fragment: Fragment, tag: String) {
    val ft = supportFragmentManager.beginTransaction()
    ft.add(android.R.id.content, fragment, tag).addToBackStack(BACK_STACK)
    ft.commit()
}

fun BaseActivity.dismiss(fragment: Fragment?, fm: FragmentManager) {
    fragment?.also {
        fm.beginTransaction().remove(it).commit()
        fm.popBackStack()
    }
}

/**
 * Don't use, not yet, only when I know how dismiss the fragment without affects the navFrag
 * */
fun BaseActivity.showLoadingScreen(msg: String = "") {
    if (containsLoading().not()) {
        LoadingScreenFragment().apply {
            val args = Bundle()
            if (msg.isNotEmpty()) {
                args.putString(LoadingScreenFragment.MESSAGE_LOADING_ARG, msg)
            }
            arguments = args
            dispatchFragment(this, LoadingScreenFragment.TAG)
            loadingList.add(this)
        }
    }
}

/**
 * Don't use, not yet, only when I know how dismiss the fragment without affects the navFrag
 * */
fun BaseActivity.hideLoadingScreen() {
    loadingList.takeIf { it.isNotEmpty() }.let { list ->
        val fragment = list?.get(0)
        dismiss(fragment, supportFragmentManager)
        loadingList.remove(fragment)
    }
}