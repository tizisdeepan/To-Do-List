package com.sample.app.todolist.todo.presentation.home

import com.sample.app.todolist.todo.data.repository.FakeTaskRepository
import com.sample.app.todolist.todo.domain.ClearAllTasksUseCase
import com.sample.app.todolist.todo.domain.CreateTasksForTestingUseCase
import com.sample.app.todolist.todo.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeViewModelTest {

    private val fakeTaskRepository = FakeTaskRepository()
    private val createTasksForTestingUseCase = CreateTasksForTestingUseCase(fakeTaskRepository)
    private val clearAllTasksUseCase = ClearAllTasksUseCase(fakeTaskRepository)
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val rule = MainDispatcherRule()


    @BeforeEach
    fun setUp() {
        viewModel = HomeViewModel(rule.getDispatcherProvider(), createTasksForTestingUseCase, clearAllTasksUseCase)
    }

    @Test
    fun testCreateEntries() = runTest {
        fakeTaskRepository.emitFlowCreateTestTasksBoolean(true)
        viewModel.createTasksForTesting()
        assert(viewModel.uiState.value.areEntriesAdded.consume() == true)
    }

    @Test
    fun testClearingEntries() = runTest {
        fakeTaskRepository.emitFlowClearAllTasksBoolean(true)
        viewModel.clearAllTasks()
        assert(viewModel.uiState.value.areEntriesCleared.consume() == true)
    }
}