package com.sample.app.todolist.todo.presentation.list.ui

interface TaskListContract {
    fun initTasksRecyclerView()
    fun fetchTasks()
    fun updatePage()
    fun scrollToTop()
    fun showGoToTop(visible: Boolean)
    fun changeCacheConfiguration(isRoom: Boolean)
    fun navigateToCreateTaskPage()
}