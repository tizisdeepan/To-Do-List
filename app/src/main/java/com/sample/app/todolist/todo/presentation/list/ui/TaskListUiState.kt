package com.sample.app.todolist.todo.presentation.list.ui

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StrikethroughSpan
import androidx.paging.PagingData
import com.sample.app.todolist.todo.common.toPrettyDate
import com.sample.app.todolist.todo.data.model.Task

data class TaskListUiState(val isLoading: Boolean = false, val taskList: PagingData<TaskUI>? = null)
data class TaskUI(val id: Int, val title: Spannable, val completed: Boolean, val createdOn: Long, val displayDate: String)

fun Task.toTaskUI(): TaskUI {
    return TaskUI(id = id, title = if (completed) {
        SpannableStringBuilder(title).apply {
            setSpan(StrikethroughSpan(), 0, title.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    } else {
        SpannableStringBuilder(title)
    }, completed = completed, createdOn = createdOn, displayDate = createdOn.toPrettyDate())
}

fun TaskUI.toTask(): Task {
    return Task(id = id, title = title.toString(), completed = completed, createdOn = createdOn)
}