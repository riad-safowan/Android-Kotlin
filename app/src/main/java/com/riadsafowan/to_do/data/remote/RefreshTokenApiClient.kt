package com.riadsafowan.to_do.data.remote


import com.riadsafowan.to_do.data.model.token.TokenModel
import com.riadsafowan.to_do.util.Const
import retrofit2.http.GET

interface RefreshTokenApiClient {

    @GET(Const.BASE_URL + "/user/refresh_token")
    suspend fun refreshToken(): TokenModel

}