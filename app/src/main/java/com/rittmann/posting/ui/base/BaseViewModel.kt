package com.rittmann.posting.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()

    val progress: LiveData<Boolean>
        get() = _progress

    /**
     *
     * By marking `block` as `suspend` this creates a suspend lambda which can call suspend
     * functions.
     *
     * @param block lambda to actually load data. It is called in the viewModelScope. Before calling the
     *              lambda the loading spinner will display, after completion or error the loading
     *              spinner will stop
     */
    fun executeAsync(dispatcher: CoroutineDispatcher = Dispatchers.IO, block: suspend () -> Unit) {
        viewModelScope.launch {
            withContext(dispatcher) {
                _progress.postValue(true)
                block()
                _progress.postValue(false)
            }
        }
    }
}