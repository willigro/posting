package com.rittmann.posting.data.dao.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rittmann.posting.data.basic.Posting
import com.rittmann.posting.data.basic.User
import com.rittmann.posting.data.dao.interfaces.PostingDao
import com.rittmann.posting.data.dao.interfaces.UserDao


@Database(entities = [User::class, Posting::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postingDao(): PostingDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "app_database"
                        ).addMigrations(MIGRATION_1_2).build()
                    }
                }
            }
            return INSTANCE
        }
    }
}


// Migration from 1 to 2, Room 2.1.0
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE tb_posting " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " title TEXT NOT NULL," +
                    " description TEXT NOT NULL," +
                    " date_time TEXT NOT NULL);"
        )
    }
}