package com.sample.app.todolist.todo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.app.todolist.todo.common.DispatcherProvider
import com.sample.app.todolist.todo.common.SingleEvent
import com.sample.app.todolist.todo.domain.ClearAllTasksUseCase
import com.sample.app.todolist.todo.domain.CreateTestTasksUseCase
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
class HomeViewModel @Inject constructor(private val dispatcherProvider: DispatcherProvider, private val createTestTasksUseCase: CreateTestTasksUseCase,
                                        private val clearAllTasksUseCase: ClearAllTasksUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun createTestEntries() {
        viewModelScope.launch(dispatcherProvider.io) {
            createTestTasksUseCase().onStart {
                _uiState.update { state -> state.copy(isLoading = true) }
            }.collectLatest {
                _uiState.update { state -> state.copy(isLoading = false, areEntriesAdded = SingleEvent(true)) }
            }
        }
    }

    fun clearAllEntries() {
        viewModelScope.launch(dispatcherProvider.io) {
            clearAllTasksUseCase().onStart {
                _uiState.update { state -> state.copy(isLoading = true) }
            }.collectLatest {
                _uiState.update { state -> state.copy(isLoading = false, areEntriesCleared = SingleEvent(true)) }
            }
        }
    }
}