package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.repository.ITaskRepository
import javax.inject.Inject

class FetchTaskByIdUseCase @Inject constructor(private val taskRepository: ITaskRepository) {

    operator fun invoke(id: Int) = taskRepository.fetchTask(id)
}