package com.sample.app.todolist

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sample.app.todolist.todo.data.database.room.TaskDao
import com.sample.app.todolist.todo.data.database.room.TaskDatabase
import com.sample.app.todolist.todo.data.model.Task
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {
    private lateinit var taskDao: TaskDao
    private lateinit var db: TaskDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()
        taskDao = db.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeTaskAndReadInList() {
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2)
        taskDao.insertTasks(tasks)
        val tasksInCache = taskDao.getAllTasks()
        assert(tasksInCache.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun updateTask() {
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2)
        taskDao.insertTasks(tasks)
        assert(tasks.isNotEmpty())

        taskDao.updateTask(task1.copy(title = "Hello_Edited", completed = true))
        val task1FromDb = taskDao.getTask(task1.id)
        assert(task1FromDb?.title == "Hello_Edited")
        assert(task1FromDb?.completed == true)
    }

    @Test
    @Throws(Exception::class)
    fun deleteTask() {
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2)
        taskDao.insertTasks(tasks)
        assert(tasks.isNotEmpty())

        taskDao.deleteTask(task1.id)
        val task1FromDb = taskDao.getTask(task1.id)
        assert(task1FromDb == null)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllTasks() {
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2)
        taskDao.insertTasks(tasks)
        assert(tasks.isNotEmpty())

        taskDao.deleteAllTasks()
        assert(taskDao.getCount() == 0)
    }

    @Test
    @Throws(Exception::class)
    fun getTask() {
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2)
        taskDao.insertTasks(tasks)
        assert(tasks.isNotEmpty())

        val task = taskDao.getTask(2)
        assert(task?.id == 2)
        assert(task?.title == "World")
        assert(task?.completed == true)
    }

    @Test
    @Throws(Exception::class)
    fun count() {
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val task3 = Task(id = 3, title = "World 2", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2, task3)
        taskDao.insertTasks(tasks)
        assert(tasks.isNotEmpty())

        assert(taskDao.getCount() == 3)
    }

    @Test
    @Throws(Exception::class)
    fun insertTask() {
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val task3 = Task(id = 3, title = "World 2", completed = true, createdOn = System.currentTimeMillis())
        taskDao.insertTask(task1)
        taskDao.insertTask(task2)
        taskDao.insertTask(task3)

        assert(taskDao.getCount() == 3)
    }

    @Test
    @Throws(Exception::class)
    fun insert2000Entries() {
        val tasks: ArrayList<Task> = ArrayList()
        for (i in 1..2000) {
            tasks.add(Task(id = i, completed = false, title = "TITLE", createdOn = System.currentTimeMillis()))
        }
        taskDao.insertTasks(tasks)

        assert(taskDao.getCount() == 2000)
    }

    @Test
    @Throws(Exception::class)
    fun insert2000EntriesAndClearAll() {
        val tasks: ArrayList<Task> = ArrayList()
        for (i in 1..2000) {
            tasks.add(Task(id = i, completed = false, title = "TITLE", createdOn = System.currentTimeMillis()))
        }
        taskDao.insertTasks(tasks)
        taskDao.deleteAllTasks()

        assert(taskDao.getCount() == 0)
    }
}