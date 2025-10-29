package com.alica.herascoreapp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alica.herascoreapp.data.MatchRepository
import com.alica.herascoreapp.data.model.Match
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class MatchListViewModel(
    private val repository: MatchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private var refreshJob: Job? = null

    init {
        startAutoRefresh()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startAutoRefresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            while (true) {
                load()
                delay(30_000)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun load(date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val matches = repository.getMatchesForDate(date.toString())
                _uiState.value = UiState.Success(matches)
            } catch (t: Throwable) {
                _uiState.value = UiState.Error(t.message ?: "Unknown error")
            }
        }
    }

    sealed interface UiState {
        data object Loading : UiState
        data class Success(val matches: List<Match>) : UiState
        data class Error(val message: String) : UiState
    }
}


