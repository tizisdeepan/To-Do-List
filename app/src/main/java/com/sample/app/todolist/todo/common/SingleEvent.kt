package com.sample.app.todolist.todo.common

class SingleEvent<T>(private val value: T) {
    private var isConsumed = false

    fun haveValue() = !isConsumed && value != null

    fun consume(): T? {
        return if (!isConsumed) {
            isConsumed = true
            value
        } else {
            null
        }
    }
}