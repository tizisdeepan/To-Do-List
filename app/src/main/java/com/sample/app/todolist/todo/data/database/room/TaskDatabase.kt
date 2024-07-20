package com.sample.app.todolist.todo.data.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.app.todolist.todo.data.model.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}