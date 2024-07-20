package com.sample.app.todolist.todo.di

import com.sample.app.todolist.todo.data.ITaskDataSource
import com.sample.app.todolist.todo.data.TaskSQLiteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @SQLiteDatabaseSource
    abstract fun provideTaskSqliteDataSource(dataSource: TaskSQLiteDataSource): ITaskDataSource
}