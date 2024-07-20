package com.sample.app.todolist.todo.util

import com.sample.app.todolist.todo.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val io: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val default: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
}