package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.repository.ITaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(private val taskRepository: ITaskRepository) {

    operator fun invoke(title: String): Flow<Boolean> = taskRepository.addTask(title)
}