package com.sample.app.todolist.todo.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.app.todolist.todo.domain.CreateTodoItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import javax.inject.Inject

@HiltViewModel
class CreateTodoViewModel @Inject constructor(private val createTodoItemUseCase: CreateTodoItemUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<CreateTodoUiState> = MutableStateFlow(CreateTodoUiState())
    val uiState = _uiState.asStateFlow()

    fun createTodoItem(title: String) = createTodoItemUseCase(title).map { created ->
        _uiState.updateAndGet { state ->
            state.copy(isLoading = false, isCreated = created)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, _uiState.value)
}