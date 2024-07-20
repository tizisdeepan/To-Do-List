package com.sample.app.todolist.todo.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.data.repository.CurrentCacheStrategy
import com.sample.app.todolist.todo.data.repository.ITaskRepository
import javax.inject.Inject

class FetchTasksUseCase @Inject constructor(private val taskRepository: ITaskRepository) {

    companion object {
        const val OFFSET = 100
    }

    operator fun invoke(): Pager<Int, Task> {
        return Pager(config = PagingConfig(pageSize = OFFSET, enablePlaceholders = false), pagingSourceFactory = { taskRepository.fetchTasks() })
    }
}