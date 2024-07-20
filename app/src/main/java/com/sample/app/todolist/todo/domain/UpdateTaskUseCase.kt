package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.data.repository.ITaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(private val taskRepository: ITaskRepository) {

    operator fun invoke(task: Task) = taskRepository.updateTask(task)
}