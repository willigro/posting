package com.rittmann.posting.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {

    protected var viewModelScopeGen: CoroutineScope? = null
    private val _progress = MutableLiveData<Boolean>()

    val progress: LiveData<Boolean>
        get() = _progress

    fun executeAsyncProgress(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        scope: CoroutineScope = GlobalScope,
        block: suspend () -> Unit
    ) {
        val s = viewModelScopeGen ?: scope
        s.launch {
            withContext(dispatcher) {
                _progress.postValue(true)
                block()
                _progress.postValue(false)
            }
        }
    }

    fun executeAsync(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        scope: CoroutineScope = GlobalScope,
        block: suspend () -> Unit
    ) {
        val s = viewModelScopeGen ?: scope
        s.launch {
            withContext(dispatcher) {
                block()
            }
        }
    }
}