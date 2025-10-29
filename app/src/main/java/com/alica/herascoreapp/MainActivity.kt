package com.alica.herascoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alica.herascoreapp.data.model.Match
import com.alica.herascoreapp.di.NetworkModule
import com.alica.herascoreapp.ui.MatchDetailsScreen
import com.alica.herascoreapp.ui.MatchDetailsViewModel
import com.alica.herascoreapp.ui.MatchListScreen
import com.alica.herascoreapp.ui.MatchListViewModel
import com.alica.herascoreapp.ui.theme.HeraScoreAppTheme
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeraScoreAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val repository = remember { NetworkModule.repository }
                    val listViewModel = remember { MatchListViewModel(repository) }
                    val detailsViewModel = remember { MatchDetailsViewModel(repository) }

                    NavHost(navController = navController, startDestination = "list") {
                        composable("list") {
                            MatchListScreen(
                                viewModel = listViewModel,
                                onMatchClick = { match ->
                                    navController.navigate("details/${match.id}")
                                }
                            )
                        }
                        composable(
                            "details/{eventId}",
                            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getString("eventId")
                            requireNotNull(eventId) { "eventId parameter not found" }
                            MatchDetailsScreen(
                                viewModel = detailsViewModel,
                                eventId = eventId
                            )
                        }
                    }
                }
            }
        }
    }
}
