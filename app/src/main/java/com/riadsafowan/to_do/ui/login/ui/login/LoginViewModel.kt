package com.riadsafowan.to_do.ui.login.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.riadsafowan.to_do.R
import com.riadsafowan.to_do.data.local.pref.UserData
import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.model.token.TokenModel
import com.riadsafowan.to_do.data.remote.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) = viewModelScope.launch {

        val result = apiRepository.login(username, password)

        if (result is com.riadsafowan.to_do.data.remote.ApiResult.Success) {
            userDataStore.saveToken(TokenModel(result.value.data?.accessToken, result.value.data?.refreshToken))

            val name = "${result.value.data?.firstName} ${result.value.data?.lastName}"
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = name))
            userDataStore.save(UserData(name, result.value.data?.email!!, result.value.data.imageUrl ?:"", true))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_name)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}