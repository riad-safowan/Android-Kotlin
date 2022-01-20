package com.riadsafowan.to_do.ui.addedittask


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadsafowan.to_do.data.Task
import com.riadsafowan.to_do.data.TaskDao
import com.riadsafowan.to_do.ui.ADD_TASK_RESULT_OK
import com.riadsafowan.to_do.ui.EDIT_TASK_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val taskDao: TaskDao
) : ViewModel() {

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.taskName ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.isImportant ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()


    fun fabSaveTasksClicked() = viewModelScope.launch {
        if(taskName.trim() != ""){
            if (task != null) {
                taskDao.update(task.copy(taskName = taskName, isImportant = taskImportance))
                addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
            } else {
                taskDao.insert(Task(taskName = taskName, isImportant = taskImportance))
                addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
            }
        }else showInvalidInputMsg("Text cannot be empty")

    }

    private fun showInvalidInputMsg(text: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMsg(text))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMsg(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }


}