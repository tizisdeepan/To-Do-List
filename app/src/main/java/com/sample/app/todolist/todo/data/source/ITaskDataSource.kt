package com.sample.app.todolist.todo.data.source

import androidx.paging.PagingSource
import com.sample.app.todolist.todo.data.model.Task
import kotlinx.coroutines.flow.Flow

interface ITaskDataSource {
    fun clearAllTasks(): Flow<Boolean>
    fun createTestTasks(): Flow<Boolean>
    fun fetchTasks(): PagingSource<Int, Task>
    fun fetchTask(id: Int): Flow<Task?>
    fun addTask(title: String): Flow<Boolean>
    fun deleteTask(id: Int): Flow<Boolean>
    fun updateTask(task: Task): Flow<Boolean>
}