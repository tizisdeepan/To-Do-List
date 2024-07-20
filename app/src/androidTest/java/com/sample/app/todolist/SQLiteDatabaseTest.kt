package com.sample.app.todolist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sample.app.todolist.todo.data.database.sqlite.TaskReaderDbHelper
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.data.source.TaskSQLiteDataSource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SQLiteDatabaseTest {
    private lateinit var db: SQLiteDatabase
    private lateinit var taskSQLiteDatabase: TaskSQLiteDataSource
    private lateinit var previousData: List<Task>

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = TaskReaderDbHelper(context, "test_${TaskReaderDbHelper.DATABASE_NAME}").writableDatabase
        taskSQLiteDatabase = TaskSQLiteDataSource(db)
        previousData = taskSQLiteDatabase.fetchAllTasksInternal()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        taskSQLiteDatabase.clearAllTasksInternal()
        previousData.forEach {
            taskSQLiteDatabase.addTaskInternal(it)
        }
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeTaskAndReadInList() {
        taskSQLiteDatabase.clearAllTasksInternal()
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2)
        tasks.forEach {
            taskSQLiteDatabase.addTaskInternal(it.title)
        }

        assert(taskSQLiteDatabase.getCount() == 2L)
    }

    @Test
    @Throws(Exception::class)
    fun updateTask() {
        taskSQLiteDatabase.clearAllTasksInternal()
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2)
        tasks.forEach {
            taskSQLiteDatabase.addTaskInternal(it.title)
        }
        assert(tasks.isNotEmpty())

        taskSQLiteDatabase.updateTaskInternal(task1.copy(title = "Hello_Edited", completed = true))
        val task1FromDb = taskSQLiteDatabase.fetchTaskInternal(task1.id)
        println(task1FromDb)
        assert(task1FromDb?.title == "Hello_Edited")
        assert(task1FromDb?.completed == true)
    }

    @Test
    @Throws(Exception::class)
    fun deleteTask() {
        taskSQLiteDatabase.clearAllTasksInternal()
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2)
        tasks.forEach {
            taskSQLiteDatabase.addTaskInternal(it.title)
        }
        assert(tasks.isNotEmpty())

        taskSQLiteDatabase.deleteTaskInternal(task1.id)
        val task1FromDb = taskSQLiteDatabase.fetchTaskInternal(task1.id)
        assert(task1FromDb == null)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllTasks() {
        taskSQLiteDatabase.clearAllTasksInternal()
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2)
        tasks.forEach {
            taskSQLiteDatabase.addTaskInternal(it.title)
        }
        assert(tasks.isNotEmpty())

        taskSQLiteDatabase.clearAllTasksInternal()
        assert(taskSQLiteDatabase.getCount() == 0L)
    }

    @Test
    @Throws(Exception::class)
    fun getTask() {
        taskSQLiteDatabase.clearAllTasksInternal()
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = false, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2)
        tasks.forEach {
            taskSQLiteDatabase.addTaskInternal(it.title)
        }
        assert(tasks.isNotEmpty())

        val task = taskSQLiteDatabase.fetchTaskInternal(2)
        println(task)
        assert(task?.id == 2)
        assert(task?.title == "World")
        assert(task?.completed == false)
    }

    @Test
    @Throws(Exception::class)
    fun count() {
        taskSQLiteDatabase.clearAllTasksInternal()
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val task3 = Task(id = 3, title = "World 2", completed = true, createdOn = System.currentTimeMillis())
        val tasks: List<Task> = listOf(task1, task2, task3)
        tasks.forEach {
            taskSQLiteDatabase.addTaskInternal(it.title)
        }
        assert(tasks.isNotEmpty())

        println(taskSQLiteDatabase.getCount())
        assert(taskSQLiteDatabase.getCount() == 3L)
    }

    @Test
    @Throws(Exception::class)
    fun insertTask() {
        taskSQLiteDatabase.clearAllTasksInternal()
        val task1 = Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis())
        val task2 = Task(id = 2, title = "World", completed = true, createdOn = System.currentTimeMillis())
        val task3 = Task(id = 3, title = "World 2", completed = true, createdOn = System.currentTimeMillis())
        taskSQLiteDatabase.addTaskInternal(task1.title)
        taskSQLiteDatabase.addTaskInternal(task2.title)
        taskSQLiteDatabase.addTaskInternal(task3.title)

        assert(taskSQLiteDatabase.getCount() == 3L)
    }

    @Test
    @Throws(Exception::class)
    fun insert2000Entries() {
        taskSQLiteDatabase.clearAllTasksInternal()
        for (i in 1..2000) {
            taskSQLiteDatabase.addTaskInternal("TITLE")
        }

        assert(taskSQLiteDatabase.getCount() == 2000L)
    }

    @Test
    @Throws(Exception::class)
    fun insert2000EntriesAndClearAll() {
        taskSQLiteDatabase.clearAllTasksInternal()
        for (i in 1..2000) {
            taskSQLiteDatabase.addTaskInternal("TITLE")
        }
        taskSQLiteDatabase.clearAllTasksInternal()

        assert(taskSQLiteDatabase.getCount() == 0L)
    }
}