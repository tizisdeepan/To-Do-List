package com.sample.app.todolist.todo.data.repository

import androidx.paging.PagingSource
import com.sample.app.todolist.todo.data.model.DatabasePerformance
import com.sample.app.todolist.todo.data.model.Task
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {
    fun clearAllTasks(): Flow<Boolean>
    fun createTestTasks(): Flow<Boolean>
    fun fetchTasks(): PagingSource<Int, Task>
    fun fetchTask(id: Int): Flow<Task?>
    fun addTask(title: String): Flow<Boolean>
    fun deleteTask(id: Int): Flow<Boolean>
    fun updateTask(task: Task): Flow<Boolean>
    fun calculateDBPerformance(): Flow<DatabasePerformance>
}