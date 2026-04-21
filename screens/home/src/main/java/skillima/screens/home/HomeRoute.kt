package skillima.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import skillima.mentors.navigation.GuildScreen
import skillima.mentors.navigation.Navigator

@Composable
fun HomeRoute() {
    val viewModel: HomeViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val logoutState by viewModel.logoutState.collectAsStateWithLifecycle()
    val navigator: Navigator = getKoin().get()

    when (val state = uiState) {
        is HomeUiState.Loading -> {
            // Splash screen already handles initial loading
            Text("Loading")
        }
        is HomeUiState.Success -> {
            HomeScreen(
                name = state.name,
                email = state.email,
                logoutState = logoutState,
                onLogout = viewModel::logout,
                onExploreGuilds = { navigator.goTo(GuildScreen) }
            )
        }
        is HomeUiState.Error -> {
            // If user session is invalid, logout clears state and Navigator re-routes
            Text("Error")
        }
    }
}
