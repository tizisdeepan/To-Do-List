package com.sample.app.todolist.todo.data.repository

import androidx.paging.PagingSource
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.data.source.TestPagingSource
import com.sample.app.todolist.todo.data.source.createPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTaskRepository : ITaskRepository {

    private val flowCreateTestTasksBoolean = MutableStateFlow(false)
    private val flowClearAllTasksBoolean = MutableStateFlow(false)
    private val flowAddTaskBoolean = MutableStateFlow(false)
    private val flowFetchTask = MutableStateFlow<Task?>(null)
    private val flowFetchTasks = MutableStateFlow<Task?>(null)
    private val flowDeleteTaskBoolean = MutableStateFlow(false)
    private val flowUpdateTaskBoolean = MutableStateFlow(false)
    suspend fun emitFlowCreateTestTasksBoolean(value: Boolean) = flowCreateTestTasksBoolean.emit(value)
    suspend fun emitFlowClearAllTasksBoolean(value: Boolean) = flowClearAllTasksBoolean.emit(value)
    suspend fun emitFlowAddTaskBoolean(value: Boolean) = flowAddTaskBoolean.emit(value)
    suspend fun emitFlowFetchTask(task: Task) = flowFetchTask.emit(task)
    suspend fun emitFlowDeleteTaskBoolean(value: Boolean) = flowDeleteTaskBoolean.emit(value)
    suspend fun emitFlowUpdateTaskBoolean(value: Boolean) = flowUpdateTaskBoolean.emit(value)

    override fun clearAllTasks(): Flow<Boolean> {
        return flowClearAllTasksBoolean
    }

    override fun createTestTasks(): Flow<Boolean> {
        return flowCreateTestTasksBoolean
    }

    override fun fetchTasks(): PagingSource<Int, Task> {
        return TestPagingSource(listOf(Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis()), Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())))
    }

    override fun fetchTask(id: Int): Flow<Task?> {
        return flowFetchTask
    }

    override fun addTask(title: String): Flow<Boolean> {
        return flowAddTaskBoolean
    }

    override fun deleteTask(id: Int): Flow<Boolean> {
        return flowDeleteTaskBoolean
    }

    override fun updateTask(task: Task): Flow<Boolean> {
        return flowUpdateTaskBoolean
    }
}