package com.sample.app.todolist.todo.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.app.todolist.todo.domain.CreateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(private val createTaskUseCase: CreateTaskUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<CreateTaskUiState> = MutableStateFlow(CreateTaskUiState())
    val uiState = _uiState.asStateFlow()

    fun createTask(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            createTaskUseCase(title).collectLatest { created ->
                _uiState.update { state ->
                    state.copy(isLoading = false, isCreated = created)
                }
            }
        }
    }
}