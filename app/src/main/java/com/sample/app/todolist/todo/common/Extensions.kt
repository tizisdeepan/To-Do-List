package com.sample.app.todolist.todo.common

import com.sample.app.todolist.todo.data.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toPrettyDate(): String {
    return Date().run {
        time = this@toPrettyDate
        SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault()).format(this)
    }
}

fun Int.get2000Tasks(): List<Task> {
    val tasks: ArrayList<Task> = ArrayList()
    for (i in this..(this + 1999)) {
        tasks.add(Task(title = EnglishNumberToWords.convert(i.toLong()), completed = false, createdOn = System.currentTimeMillis()))
    }
    return tasks
}