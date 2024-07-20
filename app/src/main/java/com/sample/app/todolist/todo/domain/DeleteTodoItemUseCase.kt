package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.ITodoDataSource
import com.sample.app.todolist.todo.di.SQLiteDatabaseSource
import javax.inject.Inject

class DeleteTodoItemUseCase @Inject constructor(@SQLiteDatabaseSource private val todoDataSource: ITodoDataSource) {

    operator fun invoke(id: Long) = todoDataSource.deleteTodo(id)
}