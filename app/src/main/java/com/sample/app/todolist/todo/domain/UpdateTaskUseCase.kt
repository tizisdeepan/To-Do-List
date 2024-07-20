package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.ITaskDataSource
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.di.SQLiteDatabaseSource
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(@SQLiteDatabaseSource private val taskDataSource: ITaskDataSource) {

    operator fun invoke(task: Task) = taskDataSource.updateTask(task)
}