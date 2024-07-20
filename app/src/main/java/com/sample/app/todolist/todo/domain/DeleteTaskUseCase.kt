package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.repository.ITaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val taskRepository: ITaskRepository) {

    operator fun invoke(id: Int) = taskRepository.deleteTask(id)
}