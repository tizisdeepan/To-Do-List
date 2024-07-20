package com.sample.app.todolist.todo.data.repository

import androidx.paging.PagingSource
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.data.source.ITaskDataSource
import com.sample.app.todolist.todo.di.RoomDatabaseSource
import com.sample.app.todolist.todo.di.SQLiteDatabaseSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(@SQLiteDatabaseSource val sqliteDatabase: ITaskDataSource, @RoomDatabaseSource val roomDatabase: ITaskDataSource) : ITaskRepository {
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
                sqliteDatabase.createTestTasks()
            }

            DatabaseStrategy.ROOM -> {
                roomDatabase.createTestTasks()
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
                sqliteDatabase.fetchTask(id)
            }

            DatabaseStrategy.ROOM -> {
                roomDatabase.fetchTask(id)
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
                sqliteDatabase.deleteTask(id)
            }

            DatabaseStrategy.ROOM -> {
                roomDatabase.deleteTask(id)
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