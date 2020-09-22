package com.rittmann.posting.ui.createaccount

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rittmann.posting.R
import com.rittmann.posting.data.dao.config.AppDatabase
import com.rittmann.posting.data.preferences.ManagerDataStore
import com.rittmann.posting.data.repository.AccountRepositoryImpl
import com.rittmann.posting.internal.isValid
import com.rittmann.posting.ui.base.BaseFragment
import com.rittmann.widgets.dialog.dialog
import kotlinx.android.synthetic.main.create_account_fragment.btnCreateAccount
import kotlinx.android.synthetic.main.create_account_fragment.edtConfirmPassword
import kotlinx.android.synthetic.main.create_account_fragment.edtNickname
import kotlinx.android.synthetic.main.create_account_fragment.edtPassword

class CreateAccountFragment : BaseFragment() {

    override var layoutId: Int = R.layout.create_account_fragment

    private val _viewModel = viewModels<CreateAccountViewModel>(factoryProducer = {
        CreateAccountViewModel.FACTORY(
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
    }

    override fun initViews() {
        btnCreateAccount.setOnClickListener {
            viewModel.register(
                edtNickname.text.toString(),
                edtPassword.text.toString(),
                edtConfirmPassword.text.toString()
            )
        }
    }

    override fun initObservers() {
        observersToValidation()

        viewModel.passwordIsDifferent.observe(viewLifecycleOwner, Observer { isDifferent ->
            edtConfirmPassword.isValid(isDifferent.not(), R.string.confirm_password)
        })

        viewModel.accountCreated.observe(viewLifecycleOwner, Observer { idCreated ->
            var execute: () -> Unit = {}
            val msg: String

            if (idCreated > 0L) {
                msg = "${getString(R.string.account_registered)} com o id $idCreated"
                execute = {
                    CreateAccountFragmentDirections.actionCreateAccountFragmentToLoginFragment()
                        .apply {
                            findNavController().navigate(this)
                        }
                }
            } else {
                msg = getString(R.string.account_register_error)
            }

            dialog(
                message = msg,
                ok = true
            ).handleShow {
                execute()
            }
        })

        progress(viewModel)
    }

    private fun observersToValidation() {
        viewModel.nicknameValidation.observe(viewLifecycleOwner, Observer { isValid ->
            edtNickname.isValid(isValid, R.string.nickname_error)
        })

        viewModel.passwordValidation.observe(viewLifecycleOwner, Observer { isValid ->
            edtPassword.isValid(isValid, R.string.password_error)
        })

        viewModel.confirmPasswordValidation.observe(viewLifecycleOwner, Observer { isValid ->
            edtConfirmPassword.isValid(isValid, R.string.confirm_password_error)
        })
    }
}
