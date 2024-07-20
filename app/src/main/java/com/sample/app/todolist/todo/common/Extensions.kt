package com.sample.app.todolist.todo.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toPrettyDate(): String {
    return Date().run {
        time = this@toPrettyDate
        SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault()).format(this)
    }
}