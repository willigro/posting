package com.rittmann.posting.ui.timeline

import androidx.lifecycle.MutableLiveData
import com.rittmann.posting.data.basic.Posting
import com.rittmann.posting.data.repository.PostingRepository
import com.rittmann.posting.ui.base.BaseViewModel
import com.rittmann.posting.ui.base.singleArgViewModelFactory

class TimelineViewModel(private val postingRepository: PostingRepository) : BaseViewModel() {
    companion object {
        val FACTORY = singleArgViewModelFactory(::TimelineViewModel)
    }

    fun postsLiveData(userId: Long) = postingRepository.getAllPostingLiveData(userId)

    val posts = MutableLiveData<List<Posting>>()

    fun getPosts(userId: Long) {
        executeAsync {
            postingRepository.getAllPosting(userId).also {
                posts.postValue(it)
            }
        }
    }
}
