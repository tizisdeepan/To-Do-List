package com.sample.app.todolist.todo.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sample.app.todolist.todo.data.model.Todo
import javax.inject.Inject

class TodoPagingSource @Inject constructor(private val fetchItems: (Long, Int) -> List<Todo>) : PagingSource<Long, Todo>() {

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Todo> {
        return try {
            val items = fetchItems(params.key ?: Long.MAX_VALUE, params.loadSize)
            Log.e("ITEMSSS", (params.key ?: Long.MAX_VALUE).toString())
            Log.e("ITEMSSS", params.loadSize.toString())
            Log.e("ITEMSSS", items.size.toString())
            Log.e("ITEMSSS", items.firstOrNull().toString())
            Log.e("ITEMSSS", items.lastOrNull().toString())
            LoadResult.Page(data = items, prevKey = null, nextKey = if (items.isNotEmpty()) items.lastOrNull()?.id else null)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Long, Todo>): Long? = null
}