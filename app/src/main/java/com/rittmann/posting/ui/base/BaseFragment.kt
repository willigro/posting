package com.rittmann.posting.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rittmann.posting.R
import com.rittmann.posting.internal.progress

abstract class BaseFragment : Fragment() {

    protected lateinit var viewRoot: View
    protected abstract var layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false).apply {
            viewRoot = this
        }
    }

    fun progress(viewModel: BaseViewModel) {
        (requireActivity() as BaseActivity).progress(viewModel)
    }

    abstract fun initViews()

    abstract fun initObservers()

    fun menuClick(execute: (view: View) -> Unit) {
        viewRoot.findViewById<View>(R.id.iconMoreToolbar).apply {
            visibility = View.VISIBLE
            setOnClickListener {
                execute(it)
            }
        }
    }

    fun setToolbar(title: String, hideButton: Boolean = false) {
        viewRoot.findViewById<View>(R.id.backButtonImage).apply {
            visibility = if (hideButton) View.INVISIBLE else View.VISIBLE
            setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
        viewRoot.findViewById<TextView>(R.id.titleOfBackButton).text = title
    }
}