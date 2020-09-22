package com.rittmann.posting.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rittmann.posting.R
import com.rittmann.posting.data.dao.config.AppDatabase
import com.rittmann.posting.data.preferences.ManagerDataStore
import com.rittmann.posting.data.repository.AccountRepositoryImpl
import com.rittmann.posting.internal.isValid
import com.rittmann.posting.internal.toast
import com.rittmann.posting.ui.base.BaseFragment
import com.rittmann.widgets.dialog.simpleLinkClick
import kotlinx.android.synthetic.main.login_fragment.btnLogin
import kotlinx.android.synthetic.main.login_fragment.checkKeepMeConnected
import kotlinx.android.synthetic.main.login_fragment.edtNickname
import kotlinx.android.synthetic.main.login_fragment.edtPassword
import kotlinx.android.synthetic.main.login_fragment.labelCreateAccount


class LoginFragment : BaseFragment() {

    override var layoutId: Int = R.layout.login_fragment

    private val _viewModel = viewModels<LoginViewModel>(factoryProducer = {
        LoginViewModel.FACTORY(
            AccountRepositoryImpl(
                AppDatabase.getDatabase(requireContext())!!,
                ManagerDataStore(requireContext())
            )
        )
    })

    private val viewModel
        get() = _viewModel.value

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
        initObservers()
        checkLogin()
    }

    override fun initViews() {
        btnLogin.setOnClickListener {
            viewModel.doLogin(edtNickname.text.toString(), edtPassword.text.toString())
        }

        handleNewAccountOption()
    }

    private fun handleNewAccountOption() {
        labelCreateAccount.simpleLinkClick(init = 24, finish = labelCreateAccount.text.length) {
            LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment().apply {
                findNavController().navigate(this)
            }
        }
    }

    override fun initObservers() {
        observersToValidation()

        viewModel.loginResult.observe(viewLifecycleOwner, Observer { user ->
            if (user == null || user.id == 0L) {
                toast(getString(R.string.login_error))

            } else {
                openTimelineScreen(user.id)

                viewModel.keepConnected(checkKeepMeConnected.isChecked)
            }
        })

        viewModel.keepLogin.observe(viewLifecycleOwner, Observer { user ->
            user?.also { openTimelineScreen(it) }
        })

        progress(viewModel)
    }

    private fun openTimelineScreen(userId: Long) {
//        LoginFragmentDirections.actionLoginFragmentToTimelineFragment(userId).apply {
//            findNavController().navigate(this)
//        }
        LoginFragmentDirections.actionLoginFragmentToTimelineActivity(userId).apply {
            findNavController().navigate(this)
        }

        toast(getString(R.string.logged_msg), Toast.LENGTH_SHORT)
    }

    private fun observersToValidation() {
        viewModel.nicknameValidation.observe(viewLifecycleOwner, Observer { isValid ->
            edtNickname.isValid(isValid, R.string.nickname_error)
        })

        viewModel.passwordValidation.observe(viewLifecycleOwner, Observer { isValid ->
            edtPassword.isValid(isValid, R.string.password_error)
        })
    }

    private fun checkLogin() {
        viewModel.isKeepConnected()
    }
}