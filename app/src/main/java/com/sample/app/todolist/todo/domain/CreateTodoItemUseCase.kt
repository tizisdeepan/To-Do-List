package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.ITodoDataSource
import com.sample.app.todolist.todo.di.SQLiteDatabaseSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateTodoItemUseCase @Inject constructor(@SQLiteDatabaseSource private val todoDataSource: ITodoDataSource) {

    operator fun invoke(title: String): Flow<Boolean> = todoDataSource.addTodo(title)
}