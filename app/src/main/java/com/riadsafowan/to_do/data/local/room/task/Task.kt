package com.riadsafowan.to_do.data.local.room.task
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.DateFormat

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val taskName: String,
    val created: Long = System.currentTimeMillis(),
    val isImportant: Boolean = false,
    val isCompleted: Boolean = false
):Serializable{
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
}
