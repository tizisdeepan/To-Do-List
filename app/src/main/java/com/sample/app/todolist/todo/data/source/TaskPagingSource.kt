package com.sample.app.todolist.todo.data.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sample.app.todolist.todo.data.model.Task
import javax.inject.Inject

class TaskPagingSource @Inject constructor(private val fetchItems: (Int, Int) -> List<Task>) : PagingSource<Int, Task>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Task> {
        return try {
            val tasks = fetchItems(params.key ?: Int.MAX_VALUE, params.loadSize)
            Log.e("ITEMSSS", (params.key ?: Int.MAX_VALUE).toString())
            Log.e("ITEMSSS", params.loadSize.toString())
            Log.e("ITEMSSS", tasks.size.toString())
            Log.e("ITEMSSS", tasks.firstOrNull().toString())
            Log.e("ITEMSSS", tasks.lastOrNull().toString())
            LoadResult.Page(data = tasks, prevKey = params.key, nextKey = if (tasks.isNotEmpty()) tasks.lastOrNull()?.id else null)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Task>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            Log.e("PAGE", "${anchorPage?.prevKey} : ${anchorPage?.nextKey}")
            anchorPage?.prevKey?.plus(1)
        }
    }

    override val keyReuseSupported: Boolean = true
}