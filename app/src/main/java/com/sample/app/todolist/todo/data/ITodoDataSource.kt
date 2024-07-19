package com.sample.app.todolist.todo.data

import com.sample.app.todolist.todo.data.model.Todo
import kotlinx.coroutines.flow.Flow

interface ITodoDataSource {
    fun addTodo(title: String): Flow<Boolean>
    fun deleteTodo(id: Long): Flow<Boolean>
    fun updateTodo(todo: Todo): Flow<Boolean>
}