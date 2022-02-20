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
import retrofit2.http.Path

interface ApiClient {

    @POST("/user/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @POST("/user/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): BaseResponse<LoginResponse>

    @POST("/user/task")
    suspend fun addTask(@Body taskRequest: TaskRequest): BaseResponse<List<TaskResponse>>

    @POST("/user/tasks")
    suspend fun addTasks(@Body taskRequest: List<TaskRequest>): BaseResponse<List<TaskResponse>>

    @GET("/user/tasks/{task_id}")
    suspend fun getTaskById(@Path("task_id") id: String): BaseResponse<TaskResponse>

    @GET("/user/tasks")
    suspend fun getTasks(): BaseResponse<List<TaskResponse>>

}