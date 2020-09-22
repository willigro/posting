package com.rittmann.posting.data.basic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rittmann.posting.data.dao.config.TableUser

@Entity(
    tableName = TableUser.TB,
    indices = [Index(value = [TableUser.ID])]
)
class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = TableUser.NICKNAME) val nickname: String?,
    @ColumnInfo(name = TableUser.PASSWORD) val password: String?
)