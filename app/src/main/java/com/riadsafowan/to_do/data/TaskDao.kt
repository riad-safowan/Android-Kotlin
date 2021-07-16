package com.riadsafowan.to_do.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {


    fun getTasks(
        searchQuery: String,
        sortOrder: SortOrder,
        hideIsCompleted: Boolean
    ): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(searchQuery, hideIsCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(searchQuery, hideIsCompleted)
        }

    @Query("SELECT * FROM task_table WHERE (isCompleted != :hideIsCompleted OR isCompleted==0) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC , taskName")
    fun getTasksSortedByName(searchQuery: String, hideIsCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (isCompleted != :hideIsCompleted OR isCompleted==0) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC , created")
    fun getTasksSortedByDateCreated(searchQuery: String, hideIsCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table WHERE isCompleted=1")
    suspend fun deleteCompletedTask()

}