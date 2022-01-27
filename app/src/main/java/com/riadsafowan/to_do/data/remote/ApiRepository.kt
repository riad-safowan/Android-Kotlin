package com.riadsafowan.to_do.data.remote

import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.model.login.LoginResponse
import com.riadsafowan.to_do.data.model.signup.SignupRequest
import com.riadsafowan.to_do.data.model.task.TaskRequest
import com.riadsafowan.to_do.data.model.token.TokenModel
import com.riadsafowan.to_do.ui.login.data.LoginDataSource
import com.riadsafowan.to_do.ui.login.data.Result
import javax.inject.Inject

open class ApiRepository @Inject constructor(
    private val apiClient: ApiClient,
    private val dataSource: LoginDataSource,
    private val userDataStore: UserDataStore
) : SafeApiCall {

    var user: LoginResponse? = null

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
            userDataStore.saveToken(TokenModel(result.data.accessToken, result.data.refreshToken))
        }

        return result
    }

    private fun setLoggedInUser(user: LoginResponse) {
        this.user = user
    }

    suspend fun signup(signupRequest: SignupRequest): Result<LoginResponse> {
        val result = dataSource.signup(signupRequest)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
            userDataStore.saveToken(TokenModel(result.data.accessToken, result.data.refreshToken))
        }

        return result
    }

    suspend fun addTask(taskRequest: TaskRequest) = safeApiCall { apiClient.addTask(taskRequest) }
    suspend fun addTasks(taskRequest: List<TaskRequest>) = safeApiCall { apiClient.addTasks(taskRequest) }
    suspend fun getTaskById(id: String) = safeApiCall { apiClient.getTaskById(id) }
    suspend fun getTasks() = safeApiCall { apiClient.getTasks() }
}