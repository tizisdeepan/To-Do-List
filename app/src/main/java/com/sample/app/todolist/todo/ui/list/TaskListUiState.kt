package com.sample.app.todolist.todo.ui.list

import androidx.paging.PagingData
import com.sample.app.todolist.todo.data.model.Task

data class TaskListUiState(val isLoading: Boolean = false, val taskList: PagingData<Task>? = null)