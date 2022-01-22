package com.riadsafowan.to_do.ui.signup

data class SignupFormState(
    val firstnameError: Int? =null,
    val lastnameError: Int? = null,
    val phnError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
