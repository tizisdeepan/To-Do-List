package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.ITaskDataSource
import com.sample.app.todolist.todo.di.SQLiteDatabaseSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(@SQLiteDatabaseSource private val taskDataSource: ITaskDataSource) {

    operator fun invoke(title: String): Flow<Boolean> = taskDataSource.addTask(title)
}