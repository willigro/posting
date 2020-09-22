package com.rittmann.posting.ui.keep

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rittmann.posting.data.basic.Posting
import com.rittmann.posting.data.repository.AccountRepository
import com.rittmann.posting.data.repository.PostingRepository
import com.rittmann.posting.ui.base.BaseViewModel
import com.rittmann.posting.ui.base.singleArgViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KeepPostViewModel(
    private val accountRepository: AccountRepository,
    private val postingRepository: PostingRepository
) : BaseViewModel() {
    companion object {
        val FACTORY = singleArgViewModelFactory(::KeepPostViewModel)
    }

    private var userId: Long = 0L

    /*
    * It isn't the best way, but i'll testing
    * */
    init {
        viewModelScope.launch {
            userId = withContext(Dispatchers.IO) {
                accountRepository.getActualUser().first()!!
            }
        }
    }

    /*
    * This way, the live data isn't protected, we can change your value from view, because
    * the view will access the value directly
    * */
    val titleValidation by lazy { MutableLiveData<Boolean>() }
    val descriptionValidation by lazy { MutableLiveData<Boolean>() }
    val postRegistered by lazy { MutableLiveData<Boolean>() }
    val postDeleted by lazy { MutableLiveData<Boolean>() }
    val postUpdated by lazy { MutableLiveData<Boolean>() }

    fun newPost(title: String?, description: String?) {
        if (isValidFields(title, description)) {
            val post = Posting(title = title!!, description = description!!, userId = userId)
            executeAsync {
                postRegistered.postValue(postingRepository.newPosting(post) > 0L)
            }
        }
    }

    fun updatePost(id: Long, title: String?, description: String?) {
        if (isValidFields(title, description)) {
            val post =
                Posting(id = id, title = title!!, description = description!!, userId = userId)
            executeAsync {
                postUpdated.postValue(postingRepository.update(post) > 0)
            }
        }
    }

    fun deletePost(post: Posting) {
        executeAsync {
            postDeleted.postValue(postingRepository.delete(post) > 0)
        }
    }

    private fun isValidFields(title: String?, description: String?): Boolean {
        titleValidation.value = title.isNullOrEmpty().not()
        descriptionValidation.value = description.isNullOrEmpty().not()

        return titleValidation.value!! && descriptionValidation.value!!
    }
}