package com.riadsafowan.to_do.ui.login.data

import com.riadsafowan.to_do.data.model.LoginRequest
import com.riadsafowan.to_do.data.model.LoginResponse
import com.riadsafowan.to_do.data.remote.ApiClient
import java.io.IOException
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        try {
            val user = apiClient.login(LoginRequest(email = username, password = password))
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }
}