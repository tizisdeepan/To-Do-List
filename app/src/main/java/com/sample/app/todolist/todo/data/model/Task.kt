package com.sample.app.todolist.todo.data.model

data class Task(val id: Long = 0, val title: String, val completed: Boolean, val createdOn: Long)