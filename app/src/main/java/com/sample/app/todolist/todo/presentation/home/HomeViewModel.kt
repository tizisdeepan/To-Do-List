package com.sample.app.todolist.todo.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.app.todolist.todo.common.DispatcherProvider
import com.sample.app.todolist.todo.common.SingleEvent
import com.sample.app.todolist.todo.domain.CalculateDatabasePerformanceUseCase
import com.sample.app.todolist.todo.domain.ClearAllTasksUseCase
import com.sample.app.todolist.todo.domain.CreateTasksForTestingUseCase
import com.sample.app.todolist.todo.presentation.home.ui.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val dispatcherProvider: DispatcherProvider, private val createTasksForTestingUseCase: CreateTasksForTestingUseCase,
                                        private val clearAllTasksUseCase: ClearAllTasksUseCase, private val calculateDatabasePerformanceUseCase: CalculateDatabasePerformanceUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun createTasksForTesting() {
        viewModelScope.launch(dispatcherProvider.io) {
            createTasksForTestingUseCase().onStart {
                _uiState.update { state -> state.copy(isLoading = true) }
            }.collectLatest {
                _uiState.update { state -> state.copy(isLoading = false, areEntriesAdded = SingleEvent(true)) }
            }
        }
    }

    fun clearAllTasks() {
        viewModelScope.launch(dispatcherProvider.io) {
            clearAllTasksUseCase().onStart {
                _uiState.update { state -> state.copy(isLoading = true) }
            }.collectLatest {
                _uiState.update { state -> state.copy(isLoading = false, areEntriesCleared = SingleEvent(true)) }
            }
        }
    }

    fun calculateDatabasePerformance() {
        viewModelScope.launch(dispatcherProvider.io) {
            calculateDatabasePerformanceUseCase().onStart {
                _uiState.value = _uiState.value.copy(isLoading = true)
            }.collectLatest {
                _uiState.update { state -> state.copy(isLoading = false, databasePerformance = it) }
            }
        }
    }
}