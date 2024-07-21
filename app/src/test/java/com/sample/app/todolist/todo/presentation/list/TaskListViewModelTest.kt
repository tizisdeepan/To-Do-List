package com.sample.app.todolist.todo.presentation.home

import androidx.paging.testing.asSnapshot
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.data.repository.FakeTaskRepository
import com.sample.app.todolist.todo.domain.FetchTasksUseCase
import com.sample.app.todolist.todo.domain.UpdateTaskUseCase
import com.sample.app.todolist.todo.presentation.list.TaskListViewModel
import com.sample.app.todolist.todo.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TaskListViewModelTest {

    private val fakeTaskRepository = FakeTaskRepository()
    private val fetchTasksUseCase = FetchTasksUseCase(fakeTaskRepository)
    private val updateTaskUseCase = UpdateTaskUseCase(fakeTaskRepository)
    private lateinit var viewModel: TaskListViewModel

    @get:Rule
    val rule = MainDispatcherRule()


    @BeforeEach
    fun setUp() {
        viewModel = TaskListViewModel(rule.getDispatcherProvider(), fetchTasksUseCase, updateTaskUseCase)
    }

    @Test
    fun `test UI State upon fetching Tasks Flow`() = runTest {
        val tasks = viewModel.fetchTasksFlow(rule.getTestScope()).asSnapshot()
        assert(tasks.isNotEmpty())
    }

    @Test
    fun `test UI State upon fetching Tasks`() = runTest {
        viewModel.fetchTasks(rule.getTestScope())
        assert(viewModel.uiState.value.taskList != null)
    }

    @Test
    fun `test UI State upon updating a Task`() = runTest {
        fakeTaskRepository.emitFlowUpdateTaskBoolean(true)
        viewModel.updateTask(Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis()))
        assert(viewModel.updateTask.value?.completed == false)
        assert(viewModel.updateTask.value?.id == 1)
    }
}