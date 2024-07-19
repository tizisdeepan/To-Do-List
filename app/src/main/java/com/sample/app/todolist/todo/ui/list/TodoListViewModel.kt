package com.sample.app.todolist.todo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sample.app.todolist.todo.domain.FetchTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(private val fetchTodoItemsUseCase: FetchTodoItemsUseCase) : ViewModel() {

    val uiState: StateFlow<TodoListUiState> = fetchTodoItemsUseCase(0).cachedIn(viewModelScope).map {
        TodoListUiState(false, it)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TodoListUiState(false, null))
}