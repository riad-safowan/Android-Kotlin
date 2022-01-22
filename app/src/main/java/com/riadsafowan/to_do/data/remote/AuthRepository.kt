package com.riadsafowan.to_do.data.remote

import com.riadsafowan.to_do.data.model.login.LoginResponse
import com.riadsafowan.to_do.data.model.signup.SignupRequest
import com.riadsafowan.to_do.ui.login.data.LoginDataSource
import com.riadsafowan.to_do.ui.login.data.Result
import javax.inject.Inject

class AuthRepository @Inject constructor(private val dataSource: LoginDataSource) {

    var user: LoginResponse? = null

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
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
        }

        return result
    }
}