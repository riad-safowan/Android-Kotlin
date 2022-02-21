package com.riadsafowan.to_do.data.model.posts

import com.google.gson.annotations.SerializedName

data class PostResponse(

	@field:SerializedName("user_image_url")
	val userImageUrl: String? = null,

	@field:SerializedName("post_id")
	val postId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("text")
	val text: String? = null
)
