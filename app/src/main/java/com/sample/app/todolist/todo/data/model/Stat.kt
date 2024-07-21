package com.sample.app.todolist.todo.data.model

data class Stat(var averageWrites: Long = -1L, var averageReads: Long = -1L, var averageDeletes: Long = -1L)
data class DatabasePerformance(val room: Stat = Stat(), val sqlite: Stat = Stat())