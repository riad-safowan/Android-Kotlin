package com.riadsafowan.to_do.ui.tasks

import androidx.lifecycle.*
import com.riadsafowan.to_do.data.local.room.task.Task
import com.riadsafowan.to_do.data.local.room.task.TaskRepository
import com.riadsafowan.to_do.data.local.pref.PreferencesRepository
import com.riadsafowan.to_do.data.local.pref.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val preferencesRepository: PreferencesRepository,
    state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    val preferencesFlow = preferencesRepository.preferencesFlow

    @ExperimentalCoroutinesApi
    private val taskFlow = combine(
        searchQuery.asFlow(),
        preferencesRepository.preferencesFlow,
    ) { query, filterPreference ->
        Pair(query, filterPreference)
    }.flatMapLatest { (searchQuery, filterPreference) ->
        taskRepository.getTasks(
            searchQuery,
            filterPreference.sortOrder.name,
            filterPreference.hideCompleted
        )
    }

    @ExperimentalCoroutinesApi
    val tasks = taskFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesRepository.updateSortOrder(sortOrder)
    }

    fun onHideCompletedSelected(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesRepository.updateHideCompleted(hideCompleted)
    }


    fun onFabAddTaskClicked() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onItemClicked(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTask(task))
    }

    fun onCheckBoxClicked(task: Task, isChecked: Boolean) =
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = isChecked))
        }

    fun onTaskSwiped(task: Task) =
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMsg(task))
        }

    fun onUndoDeletedClicked(task: Task) =
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }

    fun onDeleteAllCompletedTaskClicked() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompleteDialog)
    }
}

sealed class TasksEvent {
    object NavigateToAddTaskScreen : TasksEvent()
    data class NavigateToEditTask(val task: Task) : TasksEvent()
    data class ShowUndoDeleteTaskMsg(val task: Task) : TasksEvent()
    data class ShowTaskSavedConfirmationMsg(val msg: String) : TasksEvent()
    object NavigateToDeleteAllCompleteDialog : TasksEvent()
}

