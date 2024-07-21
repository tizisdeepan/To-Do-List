package com.sample.app.todolist.todo.data.repository

import androidx.paging.PagingSource
import com.sample.app.todolist.todo.common.get2000Tasks
import com.sample.app.todolist.todo.data.database.CurrentCacheStrategy
import com.sample.app.todolist.todo.data.database.DatabaseStrategy
import com.sample.app.todolist.todo.data.model.DatabasePerformance
import com.sample.app.todolist.todo.data.model.Stat
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.data.source.ITaskDataSource
import com.sample.app.todolist.todo.data.source.TaskRoomDataSource
import com.sample.app.todolist.todo.data.source.TaskSQLiteDataSource
import com.sample.app.todolist.todo.di.RoomDatabaseSource
import com.sample.app.todolist.todo.di.SQLiteDatabaseSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.system.measureTimeMillis

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

    override fun calculateDBPerformance(): Flow<DatabasePerformance> = flow {
        val totalAttempts = 10
        val databasePerformance = DatabasePerformance()

        //Room
        val roomStat = Stat()
        with((roomDatabase as TaskRoomDataSource).db.taskDao()) {
            val previousTasksInRoom = getAllTasks()

            //Initialise
            deleteAllTasks()

            //Writes
            var totalWrites = 0L
            for (attempt in 1..totalAttempts) {
                val tasks = 1.get2000Tasks()
                totalWrites += measureTimeMillis {
                    insertTasks(tasks)
                }
            }
            roomStat.averageWrites = totalWrites / totalAttempts.toLong()

            //Reads
            var totalReads = 0L
            for (attempt in 1..totalAttempts) {
                deleteAllTasks()
                insertTasks(1.get2000Tasks())
                totalReads += measureTimeMillis {
                    getAllTasks()
                }
            }
            roomStat.averageReads = totalReads / totalAttempts.toLong()

            //Deletes
            var totalDeletes = 0L
            for (attempt in 1..totalAttempts) {
                deleteAllTasks()
                insertTasks(1.get2000Tasks())
                totalDeletes += measureTimeMillis {
                    deleteAllTasks()
                }
            }
            roomStat.averageDeletes = totalDeletes / totalAttempts.toLong()

            deleteAllTasks()
            insertTasks(previousTasksInRoom.reversed())
        }

        //Sqlite
        val sqliteStat = Stat()
        with((sqliteDatabase as TaskSQLiteDataSource)) {
            val previousTasksInSQLite = fetchAllTasksInternal()

            //Initialise
            clearAllTasksInternal()

            //Writes
            var totalWrites = 0L
            for (attempt in 1..totalAttempts) {
                val tasks = 1.get2000Tasks()
                totalWrites += measureTimeMillis {
                    bulkAddTaskInternal(tasks)
                }
            }
            sqliteStat.averageWrites = totalWrites / totalAttempts.toLong()

            //Reads
            var totalReads = 0L
            for (attempt in 1..totalAttempts) {
                clearAllTasksInternal()
                bulkAddTaskInternal(1.get2000Tasks())
                totalReads += measureTimeMillis {
                    fetchAllTasksInternal().let {}
                }
            }
            sqliteStat.averageReads = totalReads / totalAttempts.toLong()

            //Deletes
            var totalDeletes = 0L
            for (attempt in 1..totalAttempts) {
                clearAllTasksInternal()
                bulkAddTaskInternal(1.get2000Tasks())
                totalDeletes += measureTimeMillis {
                    clearAllTasksInternal()
                }
            }
            sqliteStat.averageDeletes = totalDeletes / totalAttempts.toLong()

            clearAllTasksInternal()
            bulkAddTaskInternal(previousTasksInSQLite.reversed())
        }

        emit(databasePerformance.copy(room = roomStat, sqlite = sqliteStat))
    }.flowOn(Dispatchers.IO)
}