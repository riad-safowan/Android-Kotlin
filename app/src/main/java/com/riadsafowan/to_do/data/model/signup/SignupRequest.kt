package com.riadsafowan.to_do.data.model.signup

import com.google.gson.annotations.SerializedName

data class SignupRequest(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = "USER",

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
