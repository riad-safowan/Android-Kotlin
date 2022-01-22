package com.riadsafowan.to_do.data.remote

import com.riadsafowan.to_do.data.model.LoginRequest
import com.riadsafowan.to_do.data.model.login.LoginResponse
import com.riadsafowan.to_do.data.model.signup.SignupRequest
import com.riadsafowan.to_do.data.model.task.TaskRequest
import com.riadsafowan.to_do.data.model.task.TaskResponse
import com.riadsafowan.to_do.data.model.token.TokenModel
import com.riadsafowan.to_do.util.Const
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiClient {

    @GET(Const.BASE_URL + "/user/refresh_token")
    suspend fun refreshToken(): TokenModel

    @POST(Const.BASE_URL + "/users/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST(Const.BASE_URL + "/users/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): LoginResponse

    @POST(Const.BASE_URL + "/user/task")
    suspend fun addTask(@Body taskRequest: TaskRequest): List<TaskResponse>

}