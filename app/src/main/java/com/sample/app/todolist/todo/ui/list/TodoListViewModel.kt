package com.sample.app.todolist.todo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sample.app.todolist.todo.common.SingleEvent
import com.sample.app.todolist.todo.data.model.Todo
import com.sample.app.todolist.todo.domain.FetchTodoItemsUseCase
import com.sample.app.todolist.todo.domain.UpdateTodoItemUseCase
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
class TodoListViewModel @Inject constructor(private val fetchTodoItemsUseCase: FetchTodoItemsUseCase, private val updateTodoItemUseCase: UpdateTodoItemUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<TodoListUiState> = MutableStateFlow(TodoListUiState())
    val uiState: StateFlow<TodoListUiState> = _uiState.asStateFlow()

    fun fetchTodoItems() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchTodoItemsUseCase().flow.cachedIn(viewModelScope).collectLatest { todoList ->
                _uiState.update { state ->
                    state.copy(isLoading = false, todoList = todoList)
                }
            }
        }
    }

    fun updateTodoItem(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTodoItemUseCase(todo).collectLatest { isSuccessful ->
                if (isSuccessful) {
                    _uiState.update { state ->
                        state.copy(isLoading = false, updatedTodoItem = SingleEvent(todo))
                    }
                }
            }
        }
    }
}