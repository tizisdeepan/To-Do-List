package com.sample.app.todolist.todo.ui.details

import androidx.lifecycle.SavedStateHandle
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.data.repository.FakeTaskRepository
import com.sample.app.todolist.todo.domain.DeleteTaskUseCase
import com.sample.app.todolist.todo.domain.FetchTaskUseCase
import com.sample.app.todolist.todo.domain.UpdateTaskUseCase
import com.sample.app.todolist.todo.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TaskDetailsViewModelTest {

    private val fakeTaskRepository = FakeTaskRepository()
    private val fetchTaskUseCase = FetchTaskUseCase(fakeTaskRepository)
    private val updateTaskUseCase = UpdateTaskUseCase(fakeTaskRepository)
    private val deleteTaskUseCase = DeleteTaskUseCase(fakeTaskRepository)
    private lateinit var viewModel: TaskDetailsViewModel

    @get:Rule
    val rule = MainDispatcherRule()


    @BeforeEach
    fun setUp() {
        viewModel = TaskDetailsViewModel(SavedStateHandle().apply {
            set("ID", 1)
        }, rule.getDispatcherProvider(), fetchTaskUseCase, updateTaskUseCase, deleteTaskUseCase)
    }

    @Test
    fun `test UI State upon fetching a Task`() = runTest {
        fakeTaskRepository.emitFlowFetchTask(Task(id = 1, title = "Hello", completed = false, createdOn = System.currentTimeMillis()))
        viewModel.fetchTask()
        assert(viewModel.uiState.value.task?.id == 1)
        assert(viewModel.uiState.value.task?.title == "Hello")
        assert(viewModel.uiState.value.task?.completed == false)
    }

    @Test
    fun `test UI State upon updating a Task`() = runTest {
        fakeTaskRepository.emitFlowUpdateTaskBoolean(true)
        viewModel.updateTask(Task(id = 1, completed = false, title = "Hello", createdOn = System.currentTimeMillis()), true)
        assert(viewModel.uiState.value.isUpdated.consume() == true)
    }

    @Test
    fun `test UI State upon deleting a Task`() = runTest {
        fakeTaskRepository.emitFlowDeleteTaskBoolean(true)
        viewModel.deleteTask()
        assert(viewModel.uiState.value.isDeleted.consume() == true)
    }
}