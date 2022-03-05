package com.riadsafowan.to_do.data.model.posts.comment

import com.google.gson.annotations.SerializedName

data class CommentResponse(

	@field:SerializedName("post_id")
	val postId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("user_img_url")
	val userImgUrl: String? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("comment_id")
	val commentId: Int? = null
)
