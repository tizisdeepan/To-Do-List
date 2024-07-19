package com.sample.app.todolist.todo.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sample.app.todolist.todo.data.model.Todo
import javax.inject.Inject

class TodoPagingSource @Inject constructor(private val fetchItems: (Long, Int) -> List<Todo>) : PagingSource<Long, Todo>() {

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Todo> {
        val items = fetchItems(params.key ?: 0L, params.loadSize)
        Log.e("ITEMS", items.toString())
        return LoadResult.Page(data = items, prevKey = null, nextKey = if (items.isNotEmpty()) items.getOrNull(0)?.createdOn else null)
    }

    override fun getRefreshKey(state: PagingState<Long, Todo>): Long? = null
}