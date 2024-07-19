package com.sample.app.todolist.todo.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class TodoReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
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
        const val DATABASE_NAME = "to_do_sqlite.db"

        //Queries
        private const val SQL_CREATE_ENTRIES = "CREATE TABLE ${TodoReaderContract.TodoEntry.TABLE_NAME} (" + "${BaseColumns._ID} INTEGER PRIMARY KEY," + "${TodoReaderContract.TodoEntry.COLUMN_NAME_TITLE} TEXT," + "${TodoReaderContract.TodoEntry.COLUMN_COMPLETED} INTEGER," + "${TodoReaderContract.TodoEntry.COLUMN_CREATED_ON} LONG)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TodoReaderContract.TodoEntry.TABLE_NAME}"
    }
}