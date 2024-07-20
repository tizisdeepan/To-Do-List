package com.sample.app.todolist.todo.data.database.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class TaskReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "tasks_database.db"

        //Queries
        private const val SQL_CREATE_ENTRIES = "CREATE TABLE ${TaskReaderContract.TaskEntry.TABLE_NAME} (" + "${BaseColumns._ID} INTEGER PRIMARY KEY," + "${TaskReaderContract.TaskEntry.COLUMN_NAME_TITLE} TEXT," + "${TaskReaderContract.TaskEntry.COLUMN_COMPLETED} INTEGER," + "${TaskReaderContract.TaskEntry.COLUMN_CREATED_ON} LONG)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TaskReaderContract.TaskEntry.TABLE_NAME}"
    }
}