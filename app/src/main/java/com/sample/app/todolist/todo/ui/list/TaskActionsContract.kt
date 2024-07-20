package com.sample.app.todolist.todo.ui.list

import com.sample.app.todolist.todo.data.model.Task

interface TaskActionsContract {
    fun updateTask(task: Task)
    fun openTask(task: Task)
}