package com.riadsafowan.to_do.data.model.task

import com.google.gson.annotations.SerializedName

data class TaskRequest(

	@field:SerializedName("task_name")
	val taskName: String? = null,

	@field:SerializedName("is_important")
	val isImportant: Boolean? = null,

	@field:SerializedName("created_at")
	val createdAt: Long? = null,

	@field:SerializedName("is_completed")
	val isCompleted: Boolean? = null
)
