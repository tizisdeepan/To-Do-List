package com.sample.app.todolist.todo.data

import androidx.paging.PagingSource
import com.sample.app.todolist.todo.data.model.Todo
import kotlinx.coroutines.flow.Flow

interface ITodoDataSource {
    fun fetchTodoList(from: Long, offset: Int): PagingSource<Long, Todo>
    fun addTodo(title: String): Flow<Boolean>
    fun deleteTodo(id: Long): Flow<Boolean>
    fun updateTodo(todo: Todo): Flow<Boolean>
}