package com.sample.app.todolist.todo.presentation.list.ui

import com.sample.app.todolist.todo.data.model.Task

interface TaskActionsContract {
    fun updateTask(task: Task)
    fun openTask(id: Int)
}