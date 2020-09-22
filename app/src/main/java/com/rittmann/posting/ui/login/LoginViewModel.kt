package com.rittmann.posting.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rittmann.posting.data.basic.User
import com.rittmann.posting.data.repository.AccountRepository
import com.rittmann.posting.ui.base.BaseViewModel
import com.rittmann.posting.ui.base.SingleLiveEvent
import com.rittmann.posting.ui.base.singleArgViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: AccountRepository) : BaseViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::LoginViewModel)
    }

    private val _nicknameValidation = MutableLiveData<Boolean>()
    private val _passwordValidation = MutableLiveData<Boolean>()
    private val _loginResult = SingleLiveEvent<User>()
    private val _keepLogin = SingleLiveEvent<Long>()

    val nicknameValidation: LiveData<Boolean> get() = _nicknameValidation
    val passwordValidation: LiveData<Boolean> get() = _passwordValidation
    val loginResult: LiveData<User> get() = _loginResult
    val keepLogin: LiveData<Long> get() = _keepLogin

    fun doLogin(nickname: String?, password: String?) {
        if (fieldsIsValid(nickname, password)) {
            viewModelScope.launch {
                /*
                * Async - Await
                * to
                * withContext
                * */
                val user = withContext(Dispatchers.IO) {
                    repository.doLogin(
                        nickname!!,
                        password!!
                    )
                }

                user?.also { repository.keepActualUser(user.id) }

                _loginResult.postValue(user)
            }
        }
    }

    fun keepConnected(checked: Boolean) {
        viewModelScope.launch {
            repository.keepConnected(checked)
        }
    }

    /* Testing the use of two executeAsync */
    fun isKeepConnected() {
        executeAsync {
            repository.isKeepConnected().first()?.also {
                if (it) {
                    executeAsync {
                        _keepLogin.postValue(repository.getActualUser().first())
                    }
                }
            }
        }
    }

    private fun fieldsIsValid(
        nickname: String?,
        password: String?
    ): Boolean {
        _nicknameValidation.value = nickname.isNullOrEmpty().not()
        _passwordValidation.value = password.isNullOrEmpty().not()

        return _nicknameValidation.value!! && _passwordValidation.value!!
    }
}
