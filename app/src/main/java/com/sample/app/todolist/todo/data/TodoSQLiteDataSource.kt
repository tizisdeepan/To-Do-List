package com.sample.app.todolist.todo.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.paging.PagingSource
import com.sample.app.todolist.todo.data.database.TodoReaderContract
import com.sample.app.todolist.todo.data.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TodoSQLiteDataSource @Inject constructor(private val db: SQLiteDatabase) : ITodoDataSource {
    override fun fetchTodoList(from: Long, offset: Int): PagingSource<Long, Todo> {
        return TodoPagingSource(::fetchTodoItems)
    }

    private fun fetchTodoItems(from: Long, offset: Int): List<Todo> {
        val projection = arrayOf(BaseColumns._ID, TodoReaderContract.TodoEntry.COLUMN_NAME_TITLE, TodoReaderContract.TodoEntry.COLUMN_COMPLETED, TodoReaderContract.TodoEntry.COLUMN_CREATED_ON)
        val selection = "${TodoReaderContract.TodoEntry.COLUMN_CREATED_ON} > ?"
        val selectionArgs = arrayOf(from.toString())

        val sortOrder = "${TodoReaderContract.TodoEntry.COLUMN_CREATED_ON} DESC"

        val cursor = db.query(TodoReaderContract.TodoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, offset.toString())

        val items = mutableListOf<Todo>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(TodoReaderContract.TodoEntry.COLUMN_NAME_TITLE))
                val completed = getInt(getColumnIndexOrThrow(TodoReaderContract.TodoEntry.COLUMN_COMPLETED))
                val createdOn = getLong(getColumnIndexOrThrow(TodoReaderContract.TodoEntry.COLUMN_CREATED_ON))
                items.add(Todo(id = id, title = title, completed = completed > 0, createdOn = createdOn))
            }
        }
        cursor.close()

        return items
    }

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