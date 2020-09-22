package com.rittmann.posting.data.preferences

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ManagerDataStore(context: Context) {

    private val dataStore: DataStore<Preferences> =
        context.createDataStore(name = "appDataStore")

    suspend fun <T> setData(keep: T, preferences: Preferences.Key<T>) {
        dataStore.edit { prefs ->
            prefs[preferences] = keep
        }
    }

    fun <T> isValue(preferences: Preferences.Key<T>): Flow<T?> = dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preference ->
        preference[preferences]
    }
}

object PreferencesKeys {
    val KEEP_CONNECTED = preferencesKey<Boolean>("keepConnected")
    val ACTUAL_USER_ID = preferencesKey<Long>("actualUserId")
}