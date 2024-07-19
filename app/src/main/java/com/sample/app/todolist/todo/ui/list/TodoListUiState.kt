package com.sample.app.todolist.todo.ui.list

import androidx.paging.PagingData
import com.sample.app.todolist.todo.data.model.Todo

data class TodoListUiState(val isLoading: Boolean = false, val todoList: PagingData<Todo>? = null)