package com.riadsafowan.to_do.data.remote

import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.model.token.TokenModel
import com.riadsafowan.to_do.util.Const
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
        var request: Request = chain.request().newBuilder().addHeader(
            "Authorization",
            "Bearer " + tokenModel.accessToken!!
        ).build()
        var response = chain.proceed(request)

        when (response.code) {
            400 -> {
                //Show Bad Request Error Message
            }
            401 -> {
                print("refresh token has been changed")
                //Show UnauthorizedError Message
                request = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJFbWFpbCI6InVzZXJAc2Fmb3dhbi5jb20iLCJGaXJzdF9uYW1lIjoiVGVzdCIsIkxhc3RfbmFtZSI6IlVzZXIiLCJVaWQiOiI2MWViYWU0NDNkZDUxZWZjZDNlNjk5ZjUiLCJVc2VyX3R5cGUiOiJVU0VSIiwiVG9rZW5fdHlwZSI6ImFjY2Vzc190b2tlbiIsImV4cCI6MTY0Mjk4MTAzMn0._w6MDsL_KQLBmvfzLbOK9BpsW-Hgq4LcsJC_pQmYEAQ"
                    )
                    .build()
                response = chain.proceed(request)
            }

            403 -> {
                //Show Forbidden Message
            }

            404 -> {
                //Show NotFound Message
            }

            // ... and so on
        }
        return response
    }
}