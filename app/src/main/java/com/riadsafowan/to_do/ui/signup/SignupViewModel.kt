package com.riadsafowan.to_do.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadsafowan.to_do.R
import com.riadsafowan.to_do.data.local.pref.UserData
import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.model.signup.SignupRequest
import com.riadsafowan.to_do.data.remote.AuthRepository
import com.riadsafowan.to_do.ui.login.data.Result
import com.riadsafowan.to_do.ui.login.ui.login.LoggedInUserView
import com.riadsafowan.to_do.ui.login.ui.login.LoginFormState
import com.riadsafowan.to_do.ui.login.ui.login.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _signupForm = MutableLiveData<LoginFormState>()
    val signupFormState: LiveData<LoginFormState> = _signupForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun signup(signupRequest: SignupRequest) = viewModelScope.launch {

        val result = authRepository.signup(signupRequest)

        if (result is Result.Success) {
            val name = "${result.data.firstName} ${result.data.lastName}"
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = name))
            userDataStore.save(UserData(name, result.data.email!!, true))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun signupDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _signupForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _signupForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _signupForm.value = LoginFormState(isDataValid = true)
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