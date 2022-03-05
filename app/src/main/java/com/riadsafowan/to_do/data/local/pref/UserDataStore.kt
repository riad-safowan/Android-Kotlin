package com.riadsafowan.to_do.data.local.pref

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.riadsafowan.to_do.data.model.token.TokenModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

data class UserData(val name: String, val email: String, val imageUrl: String, val isLoggedIn: Boolean)

class UserDataStore(val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "settings")

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
        val NAME = stringPreferencesKey("Name")
        val EMAIL = stringPreferencesKey("email")
        val IMG_URL = stringPreferencesKey("image_url")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun save(userData: UserData) {
        context.dataStore.edit {
            it[NAME] = userData.name
            it[EMAIL] = userData.email
            it[IMG_URL] = userData.imageUrl
            it[IS_LOGGED_IN] = userData.isLoggedIn
        }
    }


    fun getUser(): Flow<UserData> = context.dataStore.data.map { user ->
        UserData(
            user[NAME] ?: "",
            user[EMAIL] ?: "",
            user[IMG_URL] ?: "",
            user[IS_LOGGED_IN] ?: false
        )
    }

    fun isLogged(): Flow<Boolean> = context.dataStore.data.map {
        it[IS_LOGGED_IN] ?: false
    }

    suspend fun saveToken(tokenModel: TokenModel) {
        context.dataStore.edit {
            it[ACCESS_TOKEN] = tokenModel.accessToken ?: ""
            it[REFRESH_TOKEN] = tokenModel.refreshToken ?: ""
        }
    }
    suspend fun saveImgUrl(url: String?) {
        context.dataStore.edit {
            it[IMG_URL] = url ?: ""
        }
    }

    suspend fun getToken(): TokenModel {
        val flow: Flow<TokenModel> = context.dataStore.data.map {
            TokenModel(it[ACCESS_TOKEN], it[REFRESH_TOKEN])
        }
        return flow.first()
    }

}

