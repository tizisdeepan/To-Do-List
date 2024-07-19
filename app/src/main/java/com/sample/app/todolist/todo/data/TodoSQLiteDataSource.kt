package com.sample.app.todolist.todo.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.sample.app.todolist.todo.data.database.TodoReaderContract
import com.sample.app.todolist.todo.data.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TodoSQLiteDataSource @Inject constructor(private val db: SQLiteDatabase) : ITodoDataSource {
    override fun addTodo(title: String) = flow {
        val values = ContentValues().apply {
            put(TodoReaderContract.TodoEntry.COLUMN_NAME_TITLE, title)
            put(TodoReaderContract.TodoEntry.COLUMN_COMPLETED, false)
            put(TodoReaderContract.TodoEntry.COLUMN_CREATED_ON, System.currentTimeMillis())
        }

        val newRowId = db.insert(TodoReaderContract.TodoEntry.TABLE_NAME, null, values)

        emit(newRowId != -1L)
    }.flowOn(Dispatchers.IO)

    override fun deleteTodo(id: Long) = flow {
        val selection = "${TodoReaderContract.TodoEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val deletedRows = db.delete(TodoReaderContract.TodoEntry.TABLE_NAME, selection, selectionArgs)
        emit(deletedRows >= 0)
    }

    override fun updateTodo(todo: Todo) = flow {
        val values = ContentValues().apply {
            put(TodoReaderContract.TodoEntry.COLUMN_NAME_TITLE, todo.title)
            put(TodoReaderContract.TodoEntry.COLUMN_COMPLETED, todo.completed)
        }

        val selection = "${TodoReaderContract.TodoEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(todo.id.toString())
        val count = db.update(TodoReaderContract.TodoEntry.TABLE_NAME, values, selection, selectionArgs)

        emit(count >= 0)
    }
}