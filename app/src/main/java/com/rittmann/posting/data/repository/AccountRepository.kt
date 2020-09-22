package com.rittmann.posting.data.repository

import com.rittmann.posting.data.basic.User
import com.rittmann.posting.data.dao.config.AppDatabase
import com.rittmann.posting.data.preferences.ManagerDataStore
import com.rittmann.posting.data.preferences.PreferencesKeys
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun createAccount(user: User): Long

    fun doLogin(nickname: String, password: String): User?

    suspend fun keepConnected(keep: Boolean)

    suspend fun isKeepConnected(): Flow<Boolean?>

    suspend fun keepActualUser(userId: Long)

    suspend fun getActualUser(): Flow<Long?>
}

class AccountRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val managerDataStore: ManagerDataStore
) : AccountRepository {

    override fun createAccount(user: User): Long {
        return appDatabase.userDao().insert(user)
    }

    override fun doLogin(nickname: String, password: String): User? {
        return appDatabase.userDao().findByName(nickname, password)
    }

    override suspend fun keepConnected(keep: Boolean) {
        managerDataStore.setData(keep, PreferencesKeys.KEEP_CONNECTED)
    }

    override suspend fun isKeepConnected(): Flow<Boolean?> {
        return managerDataStore.isValue(PreferencesKeys.KEEP_CONNECTED)
    }

    override suspend fun keepActualUser(userId: Long) {
        managerDataStore.setData(userId, PreferencesKeys.ACTUAL_USER_ID)
    }

    override suspend fun getActualUser(): Flow<Long?> {
        return managerDataStore.isValue(PreferencesKeys.ACTUAL_USER_ID)
    }
}