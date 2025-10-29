package com.alica.herascoreapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alica.herascoreapp.data.model.Match

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailsScreen(
    viewModel: MatchDetailsViewModel,
    eventId: String,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(eventId) {
        viewModel.load(eventId)
    }

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Match Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Surface(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val s = state) {
                is MatchDetailsViewModel.UiState.Loading -> Loading()
                is MatchDetailsViewModel.UiState.Error -> Error(s.message)
                is MatchDetailsViewModel.UiState.Success -> MatchDetails(s.match)
            }
        }
    }
}

@Composable
fun MatchDetails(match: Match) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "${match.homeTeam} vs ${match.awayTeam}", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(text = "League: ${match.league}")
        Text(text = "Venue: ${match.venue ?: "-"}")
        Text(text = "Status: ${match.status ?: match.startTime ?: "-"}")
        Spacer(Modifier.height(12.dp))
        Text(text = "Score: ${(match.homeScore ?: 0)} - ${(match.awayScore ?: 0)}", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))
        Text(text = "Home goals", style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            match.homeGoalDetails.forEach { g ->
                Text(text = "${g.minute?.toString() ?: ""}' ${g.player}")
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(text = "Away goals", style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            match.awayGoalDetails.forEach { g ->
                Text(text = "${g.minute?.toString() ?: ""}' ${g.player}")
            }
        }
    }
}
