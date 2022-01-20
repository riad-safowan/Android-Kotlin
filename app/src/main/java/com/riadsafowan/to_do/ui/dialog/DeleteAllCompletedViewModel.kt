package com.riadsafowan.to_do.ui.dialog

import androidx.lifecycle.ViewModel
import com.riadsafowan.to_do.data.local.room.task.TaskDao
import com.riadsafowan.to_do.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAllCompletedViewModel @Inject constructor(
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel(){

    fun onConfirmClicked() = applicationScope.launch {
        taskDao.deleteCompletedTask()
    }
}