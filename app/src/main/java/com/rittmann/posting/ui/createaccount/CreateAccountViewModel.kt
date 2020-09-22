package com.rittmann.posting.ui.createaccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.posting.data.basic.User
import com.rittmann.posting.data.repository.AccountRepository
import com.rittmann.posting.ui.base.BaseViewModel
import com.rittmann.posting.ui.base.singleArgViewModelFactory

class CreateAccountViewModel(private val accountRepository: AccountRepository) : BaseViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::CreateAccountViewModel)
    }

    private val _nicknameValidation = MutableLiveData<Boolean>()
    private val _passwordValidation = MutableLiveData<Boolean>()
    private val _confirmPasswordValidation = MutableLiveData<Boolean>()
    private val _passwordIsDifferent = MutableLiveData<Boolean>()
    private val _accountCreated = MutableLiveData<Long>()

    val nicknameValidation: LiveData<Boolean>
        get() = _nicknameValidation

    val passwordValidation: LiveData<Boolean>
        get() = _passwordValidation

    val confirmPasswordValidation: LiveData<Boolean>
        get() = _confirmPasswordValidation

    val passwordIsDifferent: LiveData<Boolean>
        get() = _passwordIsDifferent

    val accountCreated: LiveData<Long>
        get() = _accountCreated

    fun register(nickname: String?, password: String?, confirmPassword: String?) {
        if (fieldsIsValid(nickname, password, confirmPassword)) {

            _passwordIsDifferent.value = if (password == confirmPassword) {
                createAccount(nickname!!, password!!)
                false
            } else {
                true
            }
        }
    }

    private fun createAccount(nickname: String, password: String) {
        executeAsync {
            val id = accountRepository.createAccount(
                User(
                    nickname = nickname,
                    password = password
                )
            )
            _accountCreated.postValue(id)
        }
    }

    private fun fieldsIsValid(
        nickname: String?,
        password: String?,
        confirmPassword: String?
    ): Boolean {
        _nicknameValidation.value = nickname.isNullOrEmpty().not()
        _passwordValidation.value = password.isNullOrEmpty().not()
        _confirmPasswordValidation.value = confirmPassword.isNullOrEmpty().not()

        return _nicknameValidation.value!! && _passwordValidation.value!! && _confirmPasswordValidation.value!!
    }
}
