package com.sample.app.todolist.todo.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.app.todolist.todo.common.DispatcherProvider
import com.sample.app.todolist.todo.common.SingleEvent
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.domain.DeleteTaskByIdUseCase
import com.sample.app.todolist.todo.domain.FetchTaskByIdUseCase
import com.sample.app.todolist.todo.domain.UpdateTaskUseCase
import com.sample.app.todolist.todo.presentation.details.ui.TaskDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle, private val dispatcherProvider: DispatcherProvider, private val fetchTaskByIdUseCase: FetchTaskByIdUseCase,
                                               private val updateTaskUseCase: UpdateTaskUseCase, private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase) : ViewModel() {

    private val id: Int = savedStateHandle.get<Int>("ID") ?: -1

    private val _uiState: MutableStateFlow<TaskDetailsUiState> = MutableStateFlow(TaskDetailsUiState())
    val uiState: StateFlow<TaskDetailsUiState> = _uiState.asStateFlow()

    init {
        fetchTask()
    }

    fun fetchTask() {
        viewModelScope.launch(dispatcherProvider.io) {
            fetchTaskByIdUseCase(id).collectLatest {
                _uiState.update { state -> state.copy(task = it) }
            }
        }
    }

    fun updateTask(task: Task, completed: Boolean) {
        val updatedTask = task.copy(completed = completed)
        viewModelScope.launch(dispatcherProvider.io) {
            updateTaskUseCase(updatedTask).collectLatest {
                _uiState.update { state -> state.copy(task = updatedTask, isUpdated = SingleEvent(true)) }
            }
        }
    }

    fun deleteTask() {
        viewModelScope.launch(dispatcherProvider.io) {
            deleteTaskByIdUseCase(id).collectLatest {
                if (it) {
                    _uiState.update { state -> state.copy(isDeleted = SingleEvent(true)) }
                }
            }
        }
    }
}