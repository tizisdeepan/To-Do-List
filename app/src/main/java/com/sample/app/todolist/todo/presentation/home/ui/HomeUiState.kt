package com.sample.app.todolist.todo.presentation.home.ui

import com.sample.app.todolist.todo.common.SingleEvent

data class HomeUiState(val isLoading: Boolean = false, val areEntriesAdded: SingleEvent<Boolean?> = SingleEvent(null), val areEntriesCleared: SingleEvent<Boolean?> = SingleEvent(null))