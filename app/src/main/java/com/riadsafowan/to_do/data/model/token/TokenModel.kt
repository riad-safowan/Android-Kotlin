package com.riadsafowan.to_do.data.model.token

import com.google.gson.annotations.SerializedName

data class TokenModel(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null
)
