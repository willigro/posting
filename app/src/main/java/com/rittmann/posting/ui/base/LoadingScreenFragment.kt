package com.rittmann.posting.ui.base

import android.os.Bundle
import com.rittmann.posting.R
import com.rittmann.posting.util.AndroidUtil
import kotlinx.android.synthetic.main.loading_screen.progressTitle
import kotlinx.android.synthetic.main.loading_screen.view.loading

class LoadingScreenFragment : BaseFragment() {

    override var layoutId: Int = R.layout.loading_screen
    override fun initViews() {}
    override fun initObservers() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
        AndroidUtil.hideKeyboard(requireActivity(), viewRoot.loading)
        viewRoot.setOnClickListener(null)

        arguments?.getString(MESSAGE_LOADING_ARG)?.also {
            if (it.isNotEmpty())
                progressTitle.text = it
        }
    }

    companion object {
        const val TAG = "LoadingScreenFragmentTag"
        const val MESSAGE_LOADING_ARG = "messageLoading"
    }
}