package com.sample.app.todolist.todo.data

import androidx.paging.PagingSource
import com.sample.app.todolist.todo.data.model.Todo
import kotlinx.coroutines.flow.Flow

interface ITodoDataSource {
    fun clearAllEntries(): Flow<Boolean>
    fun createTestEntries(): Flow<Boolean>
    fun fetchTodoList(): PagingSource<Long, Todo>
    fun fetchTodo(id: Long): Flow<Todo?>
    fun addTodo(title: String): Flow<Boolean>
    fun deleteTodo(id: Long): Flow<Boolean>
    fun updateTodo(todo: Todo): Flow<Boolean>
}