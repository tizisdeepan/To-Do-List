package com.sample.app.todolist.todo.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.sample.app.todolist.todo.data.ITaskDataSource
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.di.SQLiteDatabaseSource
import javax.inject.Inject

class FetchTasksUseCase @Inject constructor(@SQLiteDatabaseSource private val taskDataSource: ITaskDataSource) {

    companion object {
        const val OFFSET = 100
    }

    operator fun invoke(): Pager<Long, Task> {
        return Pager(config = PagingConfig(pageSize = OFFSET, enablePlaceholders = false), pagingSourceFactory = { taskDataSource.fetchTasks() })
    }
}