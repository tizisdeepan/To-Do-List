package com.sample.app.todolist.todo.ui.list

import com.sample.app.todolist.todo.data.model.Todo

interface TodoActionsContract {
    fun updateTodoItem(todo: Todo)
    fun openTodoItem(todo: Todo)
}