package com.rittmann.posting.data.dao.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rittmann.posting.data.basic.Posting
import com.rittmann.posting.data.dao.config.TablePosting

@Dao
interface PostingDao {

    @Query("SELECT * FROM ${TablePosting.TB} WHERE ${TablePosting.USER_ID} = :userId")
    fun getAll(userId: Long): List<Posting>

    @Query("SELECT * FROM ${TablePosting.TB} WHERE ${TablePosting.USER_ID} = :userId")
    fun getAllLiveData(userId:Long): LiveData<List<Posting>>

    @Insert
    fun insert(posting: Posting): Long

    @Update
    fun update(post: Posting): Int

    @Delete
    fun delete(post: Posting): Int
}