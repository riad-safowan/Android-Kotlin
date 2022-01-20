package com.riadsafowan.to_do.ui.tasks

import androidx.lifecycle.*
import com.riadsafowan.to_do.data.Task
import com.riadsafowan.to_do.data.TaskDao
import com.riadsafowan.to_do.data.pref.PreferencesRepository
import com.riadsafowan.to_do.data.pref.SortOrder
import com.riadsafowan.to_do.ui.ADD_TASK_RESULT_OK
import com.riadsafowan.to_do.ui.EDIT_TASK_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    val preferencesRepository: PreferencesRepository,
    state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val tasksEvent: MutableLiveData<TasksEvent> = MutableLiveData()

    val preferencesFlow = preferencesRepository.preferencesFlow

    @ExperimentalCoroutinesApi
    private val taskFlow = combine(
        searchQuery.asFlow(),
        preferencesRepository.preferencesFlow,
    ) { query, filterPreference ->
        Pair(query, filterPreference)
    }.flatMapLatest { (searchQuery, filterPreference) ->
        taskDao.getTasks(searchQuery, filterPreference.sortOrder.name, filterPreference.hideCompleted)
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
        tasksEvent.postValue(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onItemClicked(task: Task) = viewModelScope.launch {
        tasksEvent.postValue(TasksEvent.NavigateToEditTask(task))
    }

    fun onCheckBoxClicked(task: Task, isChecked: Boolean) =
        viewModelScope.launch {
            taskDao.update(task.copy(isCompleted = isChecked))
        }

    fun onTaskSwiped(task: Task) =
        viewModelScope.launch {
            taskDao.delete(task)
            tasksEvent.postValue(TasksEvent.ShowUndoDeleteTaskMsg(task))
        }

    fun onUndoDeletedClicked(task: Task) =
        viewModelScope.launch {
            taskDao.insert(task)
        }

    fun onDeleteAllCompletedTaskClicked() = viewModelScope.launch {
        tasksEvent.postValue(TasksEvent.NavigateToDeleteAllCompleteDialog)
    }


    private fun showTaskSavedConfirmationMsg(msg: String) = viewModelScope.launch {
        tasksEvent.postValue(TasksEvent.ShowTaskSavedConfirmationMsg(msg))
    }

    fun onEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMsg("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMsg("Task updated")
        }
    }


    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTask(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMsg(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMsg(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompleteDialog : TasksEvent()
    }

}

