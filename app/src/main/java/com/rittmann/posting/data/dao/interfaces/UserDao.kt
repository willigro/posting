package com.rittmann.posting.data.dao.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.rittmann.posting.data.basic.User
import com.rittmann.posting.data.dao.config.TableUser

@Dao
interface UserDao {
    @Query("SELECT * FROM ${TableUser.TB}")
    fun getAll(): List<User>

    @Query("SELECT * FROM ${TableUser.TB} WHERE ${TableUser.ID} IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query(
        "SELECT * FROM ${TableUser.TB} WHERE ${TableUser.NICKNAME} = :nick AND ${TableUser.PASSWORD} = :password LIMIT 1"
    )
    fun findByName(nick: String, password: String): User

    @Insert
    fun insert(user: User): Long

    @Delete
    fun delete(user: User)
}
