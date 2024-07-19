package com.sample.app.todolist.todo.data.database

import android.provider.BaseColumns

object TodoReaderContract {
    object TodoEntry : BaseColumns {
        const val TABLE_NAME = "to_do"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_COMPLETED = "completed"
        const val COLUMN_CREATED_ON = "createdOn"
    }
}