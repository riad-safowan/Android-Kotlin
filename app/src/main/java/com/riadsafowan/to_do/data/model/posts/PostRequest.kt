package com.riadsafowan.to_do.data.model.posts

import com.google.gson.annotations.SerializedName

data class PostRequest(

	@field:SerializedName("text")
	val text: String? = null
)
