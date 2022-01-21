package com.riadsafowan.to_do.data.local.pref

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class UserData(val name: String, val email: String, val isLoggedIn: Boolean)

class UserDataStore(val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "settings")

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
        val NAME = stringPreferencesKey("Name")
        val EMAIL = stringPreferencesKey("email")
    }

     suspend fun save(userData: UserData) {
        context.dataStore.edit {
            it[NAME] = userData.name
            it[EMAIL] = userData.email
            it[IS_LOGGED_IN] = userData.isLoggedIn
        }
    }


    fun getUser(): Flow<UserData> = context.dataStore.data.map { user ->
        UserData(
            user[NAME] ?: "",
            user[EMAIL] ?: "",
            user[IS_LOGGED_IN] ?: false
        )
    }

     fun isLogged(): Flow<Boolean> = context.dataStore.data.map {
        it[IS_LOGGED_IN] ?: false
    }

}

