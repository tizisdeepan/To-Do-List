package com.sample.app.todolist.todo.di

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import com.sample.app.todolist.todo.data.database.room.TaskDatabase
import com.sample.app.todolist.todo.data.database.sqlite.TaskReaderDbHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideTasksDatabase(@ApplicationContext appContext: Context): SQLiteDatabase {
        return TaskReaderDbHelper(appContext).writableDatabase
    }

    @Provides
    @Singleton
    fun provideTasksRoomDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        return Room.databaseBuilder(appContext, TaskDatabase::class.java, "tasks_database").fallbackToDestructiveMigration().build()
    }
}