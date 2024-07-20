package com.sample.app.todolist.todo.data

import android.content.ContentValues
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import androidx.paging.PagingSource
import com.sample.app.todolist.todo.common.EnglishNumberToWords
import com.sample.app.todolist.todo.data.database.TodoReaderContract
import com.sample.app.todolist.todo.data.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class TodoSQLiteDataSource @Inject constructor(private val db: SQLiteDatabase) : ITodoDataSource {
    override fun clearAllEntries(): Flow<Boolean> = flow {
        db.delete(TodoReaderContract.TodoEntry.TABLE_NAME, null, null)
        emit(true)
    }

    override fun createTestEntries(): Flow<Boolean> = flow {
        val count = DatabaseUtils.queryNumEntries(db, TodoReaderContract.TodoEntry.TABLE_NAME) + 1
        for (i in count..(count + 2000)) {
            Log.e("COUNT", i.toString())
            addTodoInternal(EnglishNumberToWords.convert(i))
        }
        emit(true)
    }.flowOn(Dispatchers.IO)

    override fun fetchTodoList(): PagingSource<Long, Todo> {
        return TodoPagingSource(::fetchTodoItems)
    }

    override fun fetchTodo(id: Long): Flow<Todo?> = flow {
        val projection = arrayOf(BaseColumns._ID, TodoReaderContract.TodoEntry.COLUMN_NAME_TITLE, TodoReaderContract.TodoEntry.COLUMN_COMPLETED, TodoReaderContract.TodoEntry.COLUMN_CREATED_ON)
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.query(TodoReaderContract.TodoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null)

        var todo: Todo? = null
        with(cursor) {
            if (moveToNext()) {
                val todoId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(TodoReaderContract.TodoEntry.COLUMN_NAME_TITLE))
                val completed = getInt(getColumnIndexOrThrow(TodoReaderContract.TodoEntry.COLUMN_COMPLETED))
                val createdOn = getLong(getColumnIndexOrThrow(TodoReaderContract.TodoEntry.COLUMN_CREATED_ON))
                todo = Todo(id = todoId, title = title, completed = completed > 0, createdOn = createdOn)
            }
        }
        cursor.close()
        emit(todo)
    }.flowOn(Dispatchers.IO)

    private fun fetchTodoItems(from: Long, offset: Int): List<Todo> {
        val projection = arrayOf(BaseColumns._ID, TodoReaderContract.TodoEntry.COLUMN_NAME_TITLE, TodoReaderContract.TodoEntry.COLUMN_COMPLETED, TodoReaderContract.TodoEntry.COLUMN_CREATED_ON)
        val selection = "${BaseColumns._ID} < ?"
        val selectionArgs = arrayOf(from.toString())

        val sortOrder = "${BaseColumns._ID} DESC"

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
        emit(addTodoInternal(title))
    }.flowOn(Dispatchers.IO)

    private fun addTodoInternal(title: String): Boolean {
        val values = ContentValues().apply {
            put(TodoReaderContract.TodoEntry.COLUMN_NAME_TITLE, title)
            put(TodoReaderContract.TodoEntry.COLUMN_COMPLETED, false)
            put(TodoReaderContract.TodoEntry.COLUMN_CREATED_ON, System.currentTimeMillis())
        }

        val newRowId = db.insert(TodoReaderContract.TodoEntry.TABLE_NAME, null, values)
        return newRowId != -1L
    }

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