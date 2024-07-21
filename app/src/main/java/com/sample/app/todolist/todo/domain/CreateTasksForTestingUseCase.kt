package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.repository.ITaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateTasksForTestingUseCase @Inject constructor(private val taskRepository: ITaskRepository) {

    operator fun invoke(): Flow<Boolean> = taskRepository.createTestTasks()
}