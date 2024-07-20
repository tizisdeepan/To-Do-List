package com.sample.app.todolist.todo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.domain.FetchTasksUseCase
import com.sample.app.todolist.todo.domain.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(private val fetchTasksUseCase: FetchTasksUseCase, private val updateTaskUseCase: UpdateTaskUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<TaskListUiState> = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    private val _updateTask: MutableStateFlow<Task?> = MutableStateFlow(null)
    val updateTask: StateFlow<Task?> = _updateTask.asStateFlow()

    fun fetchTasks() {
        viewModelScope.launch {
            fetchTasksUseCase().flow.cachedIn(viewModelScope).collectLatest { todoList ->
                _uiState.update {
                    it.copy(isLoading = false, taskList = todoList)
                }
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task).collectLatest { isSuccessful ->
                if (isSuccessful) {
                    _updateTask.update {
                        task.copy()
                    }
                }
            }
        }
    }
}