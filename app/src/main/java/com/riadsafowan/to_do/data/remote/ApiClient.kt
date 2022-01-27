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

    @POST("/users/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/users/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): LoginResponse

    @POST("/user/task")
    suspend fun addTask(@Body taskRequest: TaskRequest): List<TaskResponse>

    @POST("/user/tasks")
    suspend fun addTasks(@Body taskRequest: List<TaskRequest>): List<TaskResponse>

    @GET("/user/tasks/{task_id}")
    suspend fun getTaskById(@Path("task_id") id: String): TaskResponse

    @GET("/user/tasks")
    suspend fun getTasks(): List<TaskResponse>

}