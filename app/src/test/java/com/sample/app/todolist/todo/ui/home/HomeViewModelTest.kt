package com.sample.app.todolist.todo.ui.home

import com.sample.app.todolist.todo.data.repository.FakeTaskRepository
import com.sample.app.todolist.todo.domain.ClearAllTasksUseCase
import com.sample.app.todolist.todo.domain.CreateTestTasksUseCase
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
    private val createTestTasksUseCase = CreateTestTasksUseCase(fakeTaskRepository)
    private val clearAllTasksUseCase = ClearAllTasksUseCase(fakeTaskRepository)
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val rule = MainDispatcherRule()


    @BeforeEach
    fun setUp() {
        viewModel = HomeViewModel(rule.getDispatcherProvider(), createTestTasksUseCase, clearAllTasksUseCase)
    }

    @Test
    fun testCreateEntries() = runTest {
        fakeTaskRepository.emitFlowCreateTestTasksBoolean(true)
        viewModel.createTestEntries()
        assert(viewModel.uiState.value.areEntriesAdded.consume() == true)
    }

    @Test
    fun testClearingEntries() = runTest {
        fakeTaskRepository.emitFlowClearAllTasksBoolean(true)
        viewModel.clearAllEntries()
        assert(viewModel.uiState.value.areEntriesCleared.consume() == true)
    }
}