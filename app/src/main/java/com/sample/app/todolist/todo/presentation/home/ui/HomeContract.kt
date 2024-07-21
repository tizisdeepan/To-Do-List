package com.sample.app.todolist.todo.presentation.home.ui

interface HomeContract {
    fun hostTasksPage()
    fun updateTaskList()
    fun navigateToCreateTaskPage()
    fun navigateToTaskDetailsPage(id: Int)
    fun createTasksForTesting()
    fun clearAllTasks()
}