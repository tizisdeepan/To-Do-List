package com.sample.app.todolist.todo.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.app.todolist.todo.domain.CreateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(private val createTaskUseCase: CreateTaskUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<CreateTaskUiState> = MutableStateFlow(CreateTaskUiState())
    val uiState = _uiState.asStateFlow()

    fun createTask(title: String) = createTaskUseCase(title).map { created ->
        _uiState.updateAndGet { state ->
            state.copy(isLoading = false, isCreated = created)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, _uiState.value)
}