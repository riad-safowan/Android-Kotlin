package com.riadsafowan.to_do.data.model.posts.comment

import com.google.gson.annotations.SerializedName

data class CommentRequest(

    @field:SerializedName("text")
    val text: String? = null
)
