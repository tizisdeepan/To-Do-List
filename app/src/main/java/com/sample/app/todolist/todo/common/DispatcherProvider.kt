package com.sample.app.todolist.todo.common

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val main: CoroutineDispatcher

    val io: CoroutineDispatcher

    val default: CoroutineDispatcher
}