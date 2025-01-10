package com.toufiq.factsninja.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toufiq.factsninja.data.model.Fact
import com.toufiq.factsninja.data.repository.FactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FactsViewModel(
    private val repository: FactsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<FactsUiState>(FactsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchFact()
    }

    fun fetchFact() {
        viewModelScope.launch {
            _uiState.value = FactsUiState.Loading
            repository.fetchNewFact()
                .onSuccess { fact ->
                    _uiState.value = FactsUiState.Success(fact)
                }
                .onFailure { error ->
                    _uiState.value = FactsUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}

sealed class FactsUiState {
    data object Loading : FactsUiState()
    data class Success(val fact: Fact) : FactsUiState()
    data class Error(val message: String) : FactsUiState()
} 