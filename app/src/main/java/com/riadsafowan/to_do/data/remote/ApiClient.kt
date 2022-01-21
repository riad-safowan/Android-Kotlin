package com.riadsafowan.to_do.data.remote

import com.riadsafowan.to_do.data.model.LoginRequest
import com.riadsafowan.to_do.data.model.LoginResponse
import com.riadsafowan.to_do.util.Const
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiClient {

    @POST(Const.BASE_URL + "/users/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

}