package com.sample.app.todolist.todo.data

import com.sample.app.todolist.todo.data.model.Todo
import kotlinx.coroutines.flow.Flow

class TodoRoomDataSource : ITodoDataSource {
    override fun addTodo(title: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun deleteTodo(id: Long): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun updateTodo(todo: Todo): Flow<Boolean> {
        TODO("Not yet implemented")
    }

}