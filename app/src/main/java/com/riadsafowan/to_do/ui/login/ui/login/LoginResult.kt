package com.riadsafowan.to_do.ui.login.ui.login

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)