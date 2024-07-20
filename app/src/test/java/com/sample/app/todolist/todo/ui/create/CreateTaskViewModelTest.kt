package com.sample.app.todolist.todo.ui.create

import com.sample.app.todolist.todo.data.repository.FakeTaskRepository
import com.sample.app.todolist.todo.domain.ClearAllTasksUseCase
import com.sample.app.todolist.todo.domain.CreateTaskUseCase
import com.sample.app.todolist.todo.domain.CreateTestTasksUseCase
import com.sample.app.todolist.todo.ui.home.HomeViewModel
import com.sample.app.todolist.todo.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CreateTaskViewModelTest {

    private val fakeTaskRepository = FakeTaskRepository()
    private val createTaskUseCase = CreateTaskUseCase(fakeTaskRepository)
    private lateinit var viewModel: CreateTaskViewModel

    @get:Rule
    val rule = MainDispatcherRule()

    @BeforeEach
    fun setUp() {
        viewModel = CreateTaskViewModel(rule.getDispatcherProvider(), createTaskUseCase)
    }

    @Test
    fun testUIStateUponTaskCreation() = runTest {
        fakeTaskRepository.emitFlowAddTaskBoolean(true)
        viewModel.createTask("Hello")
        assert(viewModel.uiState.value.isCreated)
    }
}