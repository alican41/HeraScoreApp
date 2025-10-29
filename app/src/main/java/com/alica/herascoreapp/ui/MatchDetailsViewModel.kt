package com.alica.herascoreapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alica.herascoreapp.data.MatchRepository
import com.alica.herascoreapp.data.model.Match
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MatchDetailsViewModel(
    private val repository: MatchRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun load(eventId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val match = repository.getMatchDetails(eventId)
                if (match != null) _uiState.value = UiState.Success(match) else _uiState.value = UiState.Error("Not found")
            } catch (t: Throwable) {
                _uiState.value = UiState.Error(t.message ?: "Unknown error")
            }
        }
    }

    sealed interface UiState {
        data object Loading : UiState
        data class Success(val match: Match) : UiState
        data class Error(val message: String) : UiState
    }
}


