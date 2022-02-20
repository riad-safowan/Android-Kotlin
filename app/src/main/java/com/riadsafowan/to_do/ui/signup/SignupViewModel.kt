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
import com.riadsafowan.to_do.data.model.token.TokenModel
import com.riadsafowan.to_do.data.remote.ApiRepository
import com.riadsafowan.to_do.data.remote.ApiResult
import com.riadsafowan.to_do.ui.login.ui.login.LoggedInUserView
import com.riadsafowan.to_do.ui.login.ui.login.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _signupForm = MutableLiveData<SignupFormState>()
    val signupFormState: LiveData<SignupFormState> = _signupForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun signup(signupRequest: SignupRequest) = viewModelScope.launch {

        val result = apiRepository.signup(signupRequest)

        if (result is ApiResult.Success) {
            userDataStore.saveToken(TokenModel(result.value.data?.accessToken, result.value.data?.refreshToken))
            val name = "${result.value.data?.firstName} ${result.value.data?.lastName}"
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = name))
            userDataStore.save(UserData(name, result.value.data?.email!!, true))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun signupDataChanged(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        if (firstName.isEmpty()) _signupForm.value =
            SignupFormState(firstnameError = R.string.invalid_name)
        else if (lastName.isEmpty()) _signupForm.value =
            SignupFormState(lastnameError = R.string.invalid_name)
        else if (!isEmailValid(email)) {
            _signupForm.value = SignupFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _signupForm.value = SignupFormState(passwordError = R.string.invalid_password)
        } else {
            _signupForm.value = SignupFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return if (email.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

}