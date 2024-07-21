package com.sample.app.todolist.todo.data.repository

import androidx.paging.PagingSource
import com.sample.app.todolist.todo.data.database.CurrentCacheStrategy
import com.sample.app.todolist.todo.data.database.DatabaseStrategy
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.data.source.ITaskDataSource
import com.sample.app.todolist.todo.di.RoomDatabaseSource
import com.sample.app.todolist.todo.di.SQLiteDatabaseSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(@SQLiteDatabaseSource private val sqliteDatabase: ITaskDataSource, @RoomDatabaseSource private val roomDatabase: ITaskDataSource) : ITaskRepository {
    override fun clearAllTasks(): Flow<Boolean> {
        return when (CurrentCacheStrategy.strategy) {
            DatabaseStrategy.SQLITE -> {
                sqliteDatabase.clearAllTasks()
            }

            DatabaseStrategy.ROOM -> {
                roomDatabase.clearAllTasks()
            }
        }
    }

    override fun createTestTasks(): Flow<Boolean> {
        return when (CurrentCacheStrategy.strategy) {
            DatabaseStrategy.SQLITE -> {
                sqliteDatabase.addTasksForTesting()
            }

            DatabaseStrategy.ROOM -> {
                roomDatabase.addTasksForTesting()
            }
        }
    }

    override fun fetchTasks(): PagingSource<Int, Task> {
        return when (CurrentCacheStrategy.strategy) {
            DatabaseStrategy.SQLITE -> {
                sqliteDatabase.fetchTasks()
            }

            DatabaseStrategy.ROOM -> {
                roomDatabase.fetchTasks()
            }
        }
    }

    override fun fetchTask(id: Int): Flow<Task?> {
        return when (CurrentCacheStrategy.strategy) {
            DatabaseStrategy.SQLITE -> {
                sqliteDatabase.fetchTaskById(id)
            }

            DatabaseStrategy.ROOM -> {
                roomDatabase.fetchTaskById(id)
            }
        }
    }

    override fun addTask(title: String): Flow<Boolean> {
        return when (CurrentCacheStrategy.strategy) {
            DatabaseStrategy.SQLITE -> {
                sqliteDatabase.addTask(title)
            }

            DatabaseStrategy.ROOM -> {
                roomDatabase.addTask(title)
            }
        }
    }

    override fun deleteTask(id: Int): Flow<Boolean> {
        return when (CurrentCacheStrategy.strategy) {
            DatabaseStrategy.SQLITE -> {
                sqliteDatabase.deleteTaskById(id)
            }

            DatabaseStrategy.ROOM -> {
                roomDatabase.deleteTaskById(id)
            }
        }
    }

    override fun updateTask(task: Task): Flow<Boolean> {
        return when (CurrentCacheStrategy.strategy) {
            DatabaseStrategy.SQLITE -> {
                sqliteDatabase.updateTask(task)
            }

            DatabaseStrategy.ROOM -> {
                roomDatabase.updateTask(task)
            }
        }
    }
}