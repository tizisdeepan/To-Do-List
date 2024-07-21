package com.sample.app.todolist.todo.presentation.home

import com.sample.app.todolist.todo.data.model.DatabasePerformance
import com.sample.app.todolist.todo.data.model.Stat
import com.sample.app.todolist.todo.data.repository.FakeTaskRepository
import com.sample.app.todolist.todo.domain.CalculateDatabasePerformanceUseCase
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
    private val calculateDatabasePerformanceUseCase = CalculateDatabasePerformanceUseCase(fakeTaskRepository)
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val rule = MainDispatcherRule()


    @BeforeEach
    fun setUp() {
        viewModel = HomeViewModel(rule.getDispatcherProvider(), createTasksForTestingUseCase, clearAllTasksUseCase, calculateDatabasePerformanceUseCase)
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

    @Test
    fun testCalculateDatabasePerformance() = runTest {
        fakeTaskRepository.emitFlowCalculateDatabasePerformance(DatabasePerformance(room = Stat(10, 12, 1), sqlite = Stat(15, 17, 2)))
        viewModel.calculateDatabasePerformance()
        assert(viewModel.uiState.value.databasePerformance.consume() != null)
    }
}