package com.sample.app.todolist.todo.data.source

import android.content.ContentValues
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.paging.PagingSource
import com.sample.app.todolist.todo.common.EnglishNumberToWords
import com.sample.app.todolist.todo.data.database.sqlite.TaskReaderContract
import com.sample.app.todolist.todo.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class TaskSQLiteDataSource @Inject constructor(val db: SQLiteDatabase) : ITaskDataSource {
    override fun clearAllTasks(): Flow<Boolean> = flow {
        clearAllTasksInternal()
        emit(true)
    }.flowOn(Dispatchers.IO)

    fun clearAllTasksInternal() {
        db.delete(TaskReaderContract.TaskEntry.TABLE_NAME, null, null)
    }

    override fun createTestTasks(): Flow<Boolean> = flow {
        val count = getCount() + 1
        for (i in count..(count + 1999)) {
            addTaskInternal(EnglishNumberToWords.convert(i))
        }
        emit(true)
    }.flowOn(Dispatchers.IO)

    override fun fetchTasks(): PagingSource<Int, Task> {
        return TaskPagingSource(this::fetchTasksInternal)
    }

    override fun fetchTask(id: Int): Flow<Task?> = flow {
        emit(fetchTaskInternal(id))
    }.flowOn(Dispatchers.IO)

    fun fetchTaskInternal(id: Int): Task? {
        val projection = arrayOf(BaseColumns._ID, TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, TaskReaderContract.TaskEntry.COLUMN_COMPLETED, TaskReaderContract.TaskEntry.COLUMN_CREATED_ON)
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.query(TaskReaderContract.TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null)

        var task: Task? = null
        with(cursor) {
            if (moveToNext()) {
                val taskId = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE))
                val completed = getInt(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_COMPLETED))
                val createdOn = getLong(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_CREATED_ON))
                task = Task(id = taskId, title = title, completed = completed > 0, createdOn = createdOn)
            }
        }
        cursor.close()
        return task
    }

    private fun fetchTasksInternal(from: Int, offset: Int): List<Task> {
        val projection = arrayOf(BaseColumns._ID, TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, TaskReaderContract.TaskEntry.COLUMN_COMPLETED, TaskReaderContract.TaskEntry.COLUMN_CREATED_ON)
        val selection = "${BaseColumns._ID} < ?"
        val selectionArgs = arrayOf(from.toString())

        val sortOrder = "${BaseColumns._ID} DESC"

        val cursor = db.query(TaskReaderContract.TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, offset.toString())

        val items = mutableListOf<Task>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE))
                val completed = getInt(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_COMPLETED))
                val createdOn = getLong(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_CREATED_ON))
                items.add(Task(id = id.toInt(), title = title, completed = completed > 0, createdOn = createdOn))
            }
        }
        cursor.close()

        return items
    }

    fun fetchAllTasksInternal(): List<Task> {
        val projection = arrayOf(BaseColumns._ID, TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, TaskReaderContract.TaskEntry.COLUMN_COMPLETED, TaskReaderContract.TaskEntry.COLUMN_CREATED_ON)
        val selection = "${BaseColumns._ID} < ?"

        val sortOrder = "${BaseColumns._ID} DESC"

        val cursor = db.query(TaskReaderContract.TaskEntry.TABLE_NAME, projection, selection, null, null, null, sortOrder)

        val items = mutableListOf<Task>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE))
                val completed = getInt(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_COMPLETED))
                val createdOn = getLong(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_CREATED_ON))
                items.add(Task(id = id.toInt(), title = title, completed = completed > 0, createdOn = createdOn))
            }
        }
        cursor.close()

        return items
    }

    fun getCount(): Long {
        return DatabaseUtils.queryNumEntries(db, TaskReaderContract.TaskEntry.TABLE_NAME)
    }

    override fun addTask(title: String) = flow {
        emit(addTaskInternal(title))
    }.flowOn(Dispatchers.IO)

    fun addTaskInternal(title: String): Boolean {
        val values = ContentValues().apply {
            put(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, title)
            put(TaskReaderContract.TaskEntry.COLUMN_COMPLETED, false)
            put(TaskReaderContract.TaskEntry.COLUMN_CREATED_ON, System.currentTimeMillis())
        }

        val newRowId = db.insert(TaskReaderContract.TaskEntry.TABLE_NAME, null, values)
        return newRowId != -1L
    }

    fun addTaskInternal(task: Task): Boolean {
        val values = ContentValues().apply {
            put(BaseColumns._ID, task.id)
            put(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, task.title)
            put(TaskReaderContract.TaskEntry.COLUMN_COMPLETED, task.completed)
            put(TaskReaderContract.TaskEntry.COLUMN_CREATED_ON, task.createdOn)
        }

        val newRowId = db.insert(TaskReaderContract.TaskEntry.TABLE_NAME, null, values)
        return newRowId != -1L
    }

    override fun deleteTask(id: Int) = flow {
        emit(deleteTaskInternal(id) >= 0)
    }.flowOn(Dispatchers.IO)

    fun deleteTaskInternal(id: Int): Int {
        val selection = "${TaskReaderContract.TaskEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(TaskReaderContract.TaskEntry.TABLE_NAME, selection, selectionArgs)
    }

    override fun updateTask(task: Task) = flow {
        emit(updateTaskInternal(task) >= 0)
    }.flowOn(Dispatchers.IO)

    fun updateTaskInternal(task: Task): Int {
        val values = ContentValues().apply {
            put(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, task.title)
            put(TaskReaderContract.TaskEntry.COLUMN_COMPLETED, task.completed)
        }

        val selection = "${TaskReaderContract.TaskEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(task.id.toString())
        return db.update(TaskReaderContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs)
    }
}