package com.riadsafowan.to_do.data.remote

import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.model.token.TokenModel
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userDataStore: UserDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var tokenModel: TokenModel
        runBlocking {
            tokenModel = userDataStore.getToken()
        }
        var originalRequest: Request = chain.request()
        if (!tokenModel.accessToken.isNullOrEmpty()) {
            val newRequest = originalRequest.newBuilder().addHeader(
                "Authorization",
                if (!originalRequest.url.toString().contains("refresh_token"))
                    "Bearer " + tokenModel.accessToken!!
                else "Bearer " + tokenModel.refreshToken!!
            ).build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(originalRequest)
    }
}