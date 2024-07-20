package com.sample.app.todolist.todo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(@PrimaryKey(autoGenerate = true) var id: Int = 0, val title: String, val completed: Boolean, val createdOn: Long)