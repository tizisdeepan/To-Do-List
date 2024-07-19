package com.sample.app.todolist.todo.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sample.app.todolist.todo.data.ITodoDataSource
import com.sample.app.todolist.todo.data.model.Todo
import com.sample.app.todolist.todo.di.SQLiteDatabaseSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchTodoItemsUseCase @Inject constructor(@SQLiteDatabaseSource private val todoDataSource: ITodoDataSource) {

    companion object {
        const val OFFSET = 100
    }

    operator fun invoke(from: Long): Flow<PagingData<Todo>> {
        return Pager(config = PagingConfig(pageSize = OFFSET)) { todoDataSource.fetchTodoList(from, OFFSET) }.flow
    }
}