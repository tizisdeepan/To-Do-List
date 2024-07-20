package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.ITaskDataSource
import com.sample.app.todolist.todo.di.SQLiteDatabaseSource
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(@SQLiteDatabaseSource private val taskDataSource: ITaskDataSource) {

    operator fun invoke(id: Long) = taskDataSource.deleteTask(id)
}