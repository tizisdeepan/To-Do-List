package com.sample.app.todolist.todo.ui.details

import com.sample.app.todolist.todo.common.SingleEvent
import com.sample.app.todolist.todo.data.model.Task

data class TaskDetailsUiState(val task: Task? = null, val isDeleted: SingleEvent<Boolean?> = SingleEvent(null), val isUpdated: SingleEvent<Boolean?> = SingleEvent(null))