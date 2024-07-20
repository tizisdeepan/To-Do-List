package com.sample.app.todolist.todo.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.app.todolist.todo.common.SingleEvent
import com.sample.app.todolist.todo.domain.DeleteTaskUseCase
import com.sample.app.todolist.todo.domain.FetchTaskUseCase
import com.sample.app.todolist.todo.domain.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle, private val fetchTaskUseCase: FetchTaskUseCase, private val updateTaskUseCase: UpdateTaskUseCase,
                                               private val deleteTaskUseCase: DeleteTaskUseCase) : ViewModel() {

    private val id: Int = savedStateHandle.get<Int>("ID") ?: -1

    private val _uiState: MutableStateFlow<TaskDetailsUiState> = MutableStateFlow(TaskDetailsUiState())
    val uiState: StateFlow<TaskDetailsUiState> = _uiState.asStateFlow()

    init {
        fetchTodo()
    }

    private fun fetchTodo() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchTaskUseCase(id).collectLatest {
                _uiState.update { state -> state.copy(task = it) }
            }
        }
    }

    fun updateTask(completed: Boolean) {
        with(_uiState.value) {
            if (task != null) {
                val updatedTask = task.copy(completed = completed)
                viewModelScope.launch(Dispatchers.IO) {
                    updateTaskUseCase(updatedTask).collectLatest {
                        _uiState.update { state -> state.copy(task = updatedTask, isUpdated = SingleEvent(true)) }
                    }
                }
            }
        }
    }

    fun deleteTodoItem() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTaskUseCase(id).collectLatest {
                if (it) {
                    _uiState.update { state -> state.copy(isDeleted = SingleEvent(true)) }
                }
            }
        }
    }
}