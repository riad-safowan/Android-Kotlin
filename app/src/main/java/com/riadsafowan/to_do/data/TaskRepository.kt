package com.riadsafowan.to_do.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaskRepository(private val taskDao: TaskDao) {
    @WorkerThread
    suspend fun insertTask(task: Task) = withContext(Dispatchers.IO) {
        taskDao.insert(task)
    }

    @WorkerThread
    suspend fun updateTask(task: Task) = withContext(Dispatchers.IO) {
        taskDao.update(task)
    }

    @WorkerThread
    fun getTasks(searchQuery: String, sort: String, hideCompleted: Boolean): Flow<List<Task>> {
        return taskDao.getTasks(searchQuery, sort, hideCompleted)
    }

    @WorkerThread
    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

}