package com.sample.app.todolist.todo.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState

open class TestPagingSource<T : Any, V : Any>(private val items: List<V>) : PagingSource<T, V>() {

    override val keyReuseSupported: Boolean
        get() = true

    override suspend fun load(params: LoadParams<T>): LoadResult<T, V> = try {
        LoadResult.Page(data = items, prevKey = null, nextKey = null)
    } catch (e: Exception) {
        e.printStackTrace()
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<T, V>): T? = null
}

fun <T : Any> List<T>.createPager() = Pager(config = PagingConfig(pageSize = 10, enablePlaceholders = false, prefetchDistance = 1)) {
    TestPagingSource(this)
}
