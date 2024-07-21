package com.sample.app.todolist.todo.data.source

import androidx.paging.PagingSource
import com.sample.app.todolist.todo.common.EnglishNumberToWords
import com.sample.app.todolist.todo.data.database.room.TaskDatabase
import com.sample.app.todolist.todo.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TaskRoomDataSource @Inject constructor(private val db: TaskDatabase) : ITaskDataSource {
    override fun clearAllTasks(): Flow<Boolean> = flow {
        db.taskDao().deleteAllTasks()
        emit(true)
    }.flowOn(Dispatchers.IO)

    override fun addTasksForTesting(): Flow<Boolean> = flow {
        val count = db.taskDao().getCount().toLong() + 1
        val tasks: ArrayList<Task> = ArrayList()
        for (i in count..(count + 1999)) {
            tasks.add(Task(title = EnglishNumberToWords.convert(i), completed = false, createdOn = System.currentTimeMillis()))
        }
        addTaskInternal(tasks)
        emit(true)
    }.flowOn(Dispatchers.IO)

    private fun addTaskInternal(tasks: List<Task>) {
        db.taskDao().insertTasks(tasks)
    }

    override fun fetchTasks(): PagingSource<Int, Task> {
        return db.taskDao().getPaginatedTasks()
    }

    override fun fetchTaskById(id: Int): Flow<Task?> = flow {
        emit(db.taskDao().getTask(id))
    }.flowOn(Dispatchers.IO)

    override fun addTask(title: String): Flow<Boolean> = flow {
        db.taskDao().insertTask(Task(title = title, completed = false, createdOn = System.currentTimeMillis()))
        emit(true)
    }.flowOn(Dispatchers.IO)

    override fun deleteTaskById(id: Int): Flow<Boolean> = flow {
        db.taskDao().deleteTask(id)
        emit(true)
    }.flowOn(Dispatchers.IO)

    override fun updateTask(task: Task): Flow<Boolean> = flow {
        db.taskDao().updateTask(task)
        emit(true)
    }.flowOn(Dispatchers.IO)
}