package com.sample.app.todolist.todo.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.app.todolist.todo.common.SingleEvent
import com.sample.app.todolist.todo.domain.ClearAllEntriesUseCase
import com.sample.app.todolist.todo.domain.CreateTestEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val createTestEntriesUseCase: CreateTestEntriesUseCase, private val clearAllEntriesUseCase: ClearAllEntriesUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun createTestEntries() {
        viewModelScope.launch(Dispatchers.IO) {
            createTestEntriesUseCase().onStart {
                _uiState.update { state -> state.copy(isLoading = true) }
            }.collectLatest {
                Log.e("ENTRIES", "ADDED 2000 ENTRIES")
                _uiState.update { state -> state.copy(isLoading = false, areEntriesAdded = SingleEvent(true)) }
            }
        }
    }

    fun clearAllEntries() {
        viewModelScope.launch(Dispatchers.IO) {
            clearAllEntriesUseCase().onStart {
                _uiState.update { state -> state.copy(isLoading = true) }
            }.collectLatest {
                Log.e("ENTRIES", "CLEARED ALL ENTRIES")
                _uiState.update { state -> state.copy(isLoading = false, areEntriesCleared = SingleEvent(true)) }
            }
        }
    }
}