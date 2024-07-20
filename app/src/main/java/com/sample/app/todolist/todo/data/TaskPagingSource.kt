package com.sample.app.todolist.todo.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sample.app.todolist.todo.data.model.Task
import javax.inject.Inject

class TaskPagingSource @Inject constructor(private val fetchItems: (Long, Int) -> List<Task>) : PagingSource<Long, Task>() {

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Task> {
        return try {
            val tasks = fetchItems(params.key ?: Long.MAX_VALUE, params.loadSize)
            Log.e("ITEMSSS", (params.key ?: Long.MAX_VALUE).toString())
            Log.e("ITEMSSS", params.loadSize.toString())
            Log.e("ITEMSSS", tasks.size.toString())
            Log.e("ITEMSSS", tasks.firstOrNull().toString())
            Log.e("ITEMSSS", tasks.lastOrNull().toString())
            LoadResult.Page(data = tasks, prevKey = null, nextKey = if (tasks.isNotEmpty()) tasks.lastOrNull()?.id else null)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Long, Task>): Long? = null
}