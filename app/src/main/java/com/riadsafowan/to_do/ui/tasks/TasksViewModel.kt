package com.riadsafowan.to_do.ui.tasks

import androidx.lifecycle.*
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
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    private val taskFlow = taskDao.getTasks("", "", false)

    val tasks = taskFlow.asLiveData()


    fun onFabAddTaskClicked() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onItemClicked(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTask(task))
    }

    fun onCheckBoxClicked(task: Task, isChecked: Boolean) =
        viewModelScope.launch {
            taskDao.update(task.copy(isCompleted = isChecked))
        }

    fun onTaskSwiped(task: Task) =
        viewModelScope.launch {
            taskDao.delete(task)
            tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMsg(task))
        }

    fun onUndoDeletedClicked(task: Task) =
        viewModelScope.launch {
            taskDao.insert(task)
        }

    fun onDeleteAllCompletedTaskClicked() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompleteDialog)
    }


    fun onEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMsg("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMsg("Task updated")
        }
    }

    private fun showTaskSavedConfirmationMsg(msg: String) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMsg(msg))
    }

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTask(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMsg(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMsg(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompleteDialog : TasksEvent()

    }

}

