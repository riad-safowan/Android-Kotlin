package com.riadsafowan.to_do.ui.login.data

import com.riadsafowan.to_do.data.model.LoginResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(private val dataSource: LoginDataSource) {

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
}