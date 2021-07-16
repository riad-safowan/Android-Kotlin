package com.riadsafowan.to_do.data
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val taskName: String,
    val created: Long = System.currentTimeMillis(),
    val isImportant: Boolean = false,
    val isCompleted: Boolean = false
): Parcelable{
    val createdDateFormatted: String
    get() = DateFormat.getDateTimeInstance().format(created)
}
