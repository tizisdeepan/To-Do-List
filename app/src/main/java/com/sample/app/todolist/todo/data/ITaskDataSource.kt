package com.sample.app.todolist.todo.data

import androidx.paging.PagingSource
import com.sample.app.todolist.todo.data.model.Task
import kotlinx.coroutines.flow.Flow

interface ITaskDataSource {
    fun clearAllTasks(): Flow<Boolean>
    fun createTestTasks(): Flow<Boolean>
    fun fetchTasks(): PagingSource<Long, Task>
    fun fetchTask(id: Long): Flow<Task?>
    fun addTask(title: String): Flow<Boolean>
    fun deleteTask(id: Long): Flow<Boolean>
    fun updateTask(task: Task): Flow<Boolean>
}