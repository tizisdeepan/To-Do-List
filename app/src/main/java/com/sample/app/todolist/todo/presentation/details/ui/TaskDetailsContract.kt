package com.sample.app.todolist.todo.presentation.details.ui

import com.sample.app.todolist.todo.data.model.Task

interface TaskDetailsContract {
    fun setNavigation()
    fun renderTask(task: Task)
    fun updateTasksInHomePage()
    fun navigateBack()
    fun deleteTask()
    fun updateTask(completed: Boolean)
}