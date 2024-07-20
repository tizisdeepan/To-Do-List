package com.sample.app.todolist.todo.ui.details

import com.sample.app.todolist.todo.common.SingleEvent
import com.sample.app.todolist.todo.data.model.Todo

data class TodoDetailsUiState(val todo: Todo? = null, val isDeleted: SingleEvent<Boolean?> = SingleEvent(null))