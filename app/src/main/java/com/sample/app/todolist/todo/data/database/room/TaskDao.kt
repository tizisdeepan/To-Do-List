package com.sample.app.todolist.todo.data.database.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sample.app.todolist.todo.data.model.Task

@Dao
abstract class TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTasks(tasks: List<Task>)

    @Query("SELECT * FROM Task ORDER BY id DESC")
    abstract fun getPaginatedTasks(): PagingSource<Int, Task>

    @Query("SELECT * FROM Task ORDER BY id DESC")
    abstract fun getAllTasks(): List<Task>

    @Query("SELECT COUNT(*) FROM Task")
    abstract fun getCount(): Int

    @Query("SELECT * FROM Task WHERE id = :id")
    abstract fun getTask(id: Int): Task?

    @Query("DELETE FROM Task WHERE id = :id")
    abstract fun deleteTask(id: Int)

    @Update
    abstract fun updateTask(task: Task)

    @Query("DELETE FROM Task")
    abstract fun deleteAllTasks()
}