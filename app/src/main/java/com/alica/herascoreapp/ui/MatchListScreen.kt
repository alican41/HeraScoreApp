package com.alica.herascoreapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alica.herascoreapp.data.model.Match

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchListScreen(
    viewModel: MatchListViewModel,
    onMatchClick: (Match) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HeraScore") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Surface(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (state) {
                is MatchListViewModel.UiState.Loading -> Loading()
                is MatchListViewModel.UiState.Error -> Error((state as MatchListViewModel.UiState.Error).message)
                is MatchListViewModel.UiState.Success -> MatchList(
                    matches = (state as MatchListViewModel.UiState.Success).matches,
                    onMatchClick = onMatchClick
                )
            }
        }
    }
}

@Composable
fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) { CircularProgressIndicator() }
}

@Composable
fun Error(message: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) { Text(text = message, style = MaterialTheme.typography.bodyLarge) }
}

@Composable
private fun MatchList(matches: List<Match>, onMatchClick: (Match) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(matches) { match ->
            MatchRow(match = match, onClick = { onMatchClick(match) })
        }
    }
}

@Composable
private fun MatchRow(match: Match, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = match.league, style = MaterialTheme.typography.labelLarge)
            Text(text = match.status ?: match.startTime.orEmpty(), style = MaterialTheme.typography.labelMedium)
        }
        Spacer(Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f)) {
                Text(text = match.homeTeam, style = MaterialTheme.typography.titleMedium)
                Text(text = match.awayTeam, style = MaterialTheme.typography.titleMedium)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = (match.homeScore ?: 0).toString(), style = MaterialTheme.typography.titleLarge)
                Text(text = (match.awayScore ?: 0).toString(), style = MaterialTheme.typography.titleLarge)
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(text = match.venue ?: "", style = MaterialTheme.typography.bodySmall)
    }
}


