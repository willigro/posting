package com.rittmann.posting.data.repository

import androidx.lifecycle.LiveData
import com.rittmann.posting.data.basic.Posting
import com.rittmann.posting.data.dao.config.AppDatabase

interface PostingRepository {

    fun getAllPosting(userId: Long): List<Posting>
    fun getAllPostingLiveData(userId: Long): LiveData<List<Posting>>
    fun newPosting(posting: Posting): Long
    fun update(post: Posting): Int
    fun delete(post: Posting): Int
}

class PostingRepositoryImpl(private val appDatabase: AppDatabase) : PostingRepository {
    override fun getAllPosting(userId: Long): List<Posting> {
        return appDatabase.postingDao().getAll(userId)
    }

    override fun getAllPostingLiveData(userId: Long): LiveData<List<Posting>> {
        return appDatabase.postingDao().getAllLiveData(userId)
    }

    override fun newPosting(posting: Posting): Long {
        return appDatabase.postingDao().insert(posting)
    }

    override fun update(post: Posting): Int {
        return appDatabase.postingDao().update(post)
    }

    override fun delete(post: Posting): Int {
        return appDatabase.postingDao().delete(post)
    }
}