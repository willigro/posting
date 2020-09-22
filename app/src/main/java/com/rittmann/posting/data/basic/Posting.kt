package com.rittmann.posting.data.basic

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rittmann.posting.data.dao.config.TablePosting
import kotlinx.android.parcel.Parcelize
import java.util.Calendar

@Parcelize
@Entity(tableName = TablePosting.TB)
class Posting(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,

    @ColumnInfo(name = TablePosting.TITLE)
    @NonNull
    val title: String,

    @ColumnInfo(name = TablePosting.DESCRIPTION)
    @NonNull
    val description: String,

    @ColumnInfo(name = TablePosting.DATE_TIME)
    @NonNull
    val dateTime: Calendar = Calendar.getInstance(),

    @ColumnInfo(name = TablePosting.USER_ID)
    @NonNull
    val userId: Long = 0
) : Parcelable {

    fun sameContent(other: Posting): Boolean {
        return title == other.title && description == other.description
    }
}