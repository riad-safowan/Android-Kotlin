package com.riadsafowan.to_do.data.model.task

import com.google.gson.annotations.SerializedName

data class TaskResponse(

	@field:SerializedName("task_name")
	val taskName: String? = null,

	@field:SerializedName("is_important")
	val isImportant: Boolean? = null,

	@field:SerializedName("created_at")
	val createdAt: Long? = null,

	@field:SerializedName("task_id")
	val taskId: String? = null,

	@field:SerializedName("ID")
	val iD: String? = null,

	@field:SerializedName("is_completed")
	val isCompleted: Boolean? = null
)
