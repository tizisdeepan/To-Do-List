package com.sample.app.todolist.todo.data.database

import android.provider.BaseColumns

object TaskReaderContract {
    object TaskEntry : BaseColumns {
        const val TABLE_NAME = "task"
        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_COMPLETED = "completed"
        const val COLUMN_CREATED_ON = "createdOn"
    }
}