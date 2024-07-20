package com.sample.app.todolist.todo.data

import android.content.ContentValues
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import androidx.paging.PagingSource
import com.sample.app.todolist.todo.common.EnglishNumberToWords
import com.sample.app.todolist.todo.data.database.TaskReaderContract
import com.sample.app.todolist.todo.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class TaskSQLiteDataSource @Inject constructor(private val db: SQLiteDatabase) : ITaskDataSource {
    override fun clearAllTasks(): Flow<Boolean> = flow {
        db.delete(TaskReaderContract.TaskEntry.TABLE_NAME, null, null)
        emit(true)
    }

    override fun createTestTasks(): Flow<Boolean> = flow {
        val count = DatabaseUtils.queryNumEntries(db, TaskReaderContract.TaskEntry.TABLE_NAME) + 1
        for (i in count..(count + 2000)) {
            Log.e("COUNT", i.toString())
            addTaskInternal(EnglishNumberToWords.convert(i))
        }
        emit(true)
    }.flowOn(Dispatchers.IO)

    override fun fetchTasks(): PagingSource<Long, Task> {
        return TaskPagingSource(this::fetchTasksInternal)
    }

    override fun fetchTask(id: Long): Flow<Task?> = flow {
        val projection = arrayOf(BaseColumns._ID, TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, TaskReaderContract.TaskEntry.COLUMN_COMPLETED, TaskReaderContract.TaskEntry.COLUMN_CREATED_ON)
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.query(TaskReaderContract.TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null)

        var task: Task? = null
        with(cursor) {
            if (moveToNext()) {
                val taskId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE))
                val completed = getInt(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_COMPLETED))
                val createdOn = getLong(getColumnIndexOrThrow(TaskReaderContract.TaskEntry.COLUMN_CREATED_ON))
                task = Task(id = taskId, title = title, completed = completed > 0, createdOn = createdOn)
            }
        }
        cursor.close()
        emit(task)
    }.flowOn(Dispatchers.IO)

    private fun fetchTasksInternal(from: Long, offset: Int): List<Task> {
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
                items.add(Task(id = id, title = title, completed = completed > 0, createdOn = createdOn))
            }
        }
        cursor.close()

        return items
    }

    override fun addTask(title: String) = flow {
        emit(addTaskInternal(title))
    }.flowOn(Dispatchers.IO)

    private fun addTaskInternal(title: String): Boolean {
        val values = ContentValues().apply {
            put(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, title)
            put(TaskReaderContract.TaskEntry.COLUMN_COMPLETED, false)
            put(TaskReaderContract.TaskEntry.COLUMN_CREATED_ON, System.currentTimeMillis())
        }

        val newRowId = db.insert(TaskReaderContract.TaskEntry.TABLE_NAME, null, values)
        return newRowId != -1L
    }

    override fun deleteTask(id: Long) = flow {
        val selection = "${TaskReaderContract.TaskEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val deletedRows = db.delete(TaskReaderContract.TaskEntry.TABLE_NAME, selection, selectionArgs)
        emit(deletedRows >= 0)
    }

    override fun updateTask(task: Task) = flow {
        val values = ContentValues().apply {
            put(TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE, task.title)
            put(TaskReaderContract.TaskEntry.COLUMN_COMPLETED, task.completed)
        }

        val selection = "${TaskReaderContract.TaskEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(task.id.toString())
        val count = db.update(TaskReaderContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs)

        emit(count >= 0)
    }
}