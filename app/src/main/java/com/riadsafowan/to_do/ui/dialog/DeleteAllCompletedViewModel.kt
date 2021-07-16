package com.riadsafowan.to_do.ui.dialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.riadsafowan.to_do.data.TaskDao
import com.riadsafowan.to_do.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteAllCompletedViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel(){

    fun onConfirmClicked() = applicationScope.launch {
        taskDao.deleteCompletedTask()
    }
}