package com.sample.app.todolist.todo.domain

import com.sample.app.todolist.todo.data.model.DatabasePerformance
import com.sample.app.todolist.todo.data.repository.ITaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CalculateDatabasePerformanceUseCase @Inject constructor(private val taskRepository: ITaskRepository) {

    operator fun invoke(): Flow<DatabasePerformance> = taskRepository.calculateDBPerformance()
}