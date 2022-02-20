package com.riadsafowan.to_do.data.remote

import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.model.token.TokenModel
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val apiClient: RefreshTokenApiClient,
    private val userDataStore: UserDataStore
) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            when (val tokenResponse = getUpdatedToken()) {
                is ApiResult.Success -> {
                    userDataStore.saveToken(
                        TokenModel(
                            tokenResponse.value.accessToken!!,
                            tokenResponse.value.refreshToken!!
                        )
                    )
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${tokenResponse.value.accessToken}")
                        .build()
                }
                else -> null
            }
        }
    }

    private suspend fun getUpdatedToken(): ApiResult<TokenModel> {
        try {
            return ApiResult.Success(apiClient.refreshToken())
        } catch (e: Exception) {
            return ApiResult.Failure(false, 0, null)
        }
    }
}