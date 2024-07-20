package com.sample.app.todolist.todo.di

import com.sample.app.todolist.todo.data.repository.ITaskRepository
import com.sample.app.todolist.todo.data.repository.TaskRepository
import com.sample.app.todolist.todo.data.source.ITaskDataSource
import com.sample.app.todolist.todo.data.source.TaskRoomDataSource
import com.sample.app.todolist.todo.data.source.TaskSQLiteDataSource
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

    @Binds
    @RoomDatabaseSource
    abstract fun provideTaskRoomDataSource(dataSource: TaskRoomDataSource): ITaskDataSource

    @Binds
    abstract fun provideTaskRepository(repository: TaskRepository): ITaskRepository
}