package skillima.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    LaunchedEffect(logoutState) {
        if (logoutState is LogoutState.Done) {
            navigator.backStack.clear()
        }
    }

    when (val state = uiState) {
        is HomeUiState.Loading -> {
            HomeShimmerSkeleton()
        }
        is HomeUiState.AwaitingVerification -> {
            MentorAwaitingScreen(
                onLogout = viewModel::logout,
                logoutState = logoutState,
            )
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
            Text("Error")
        }
    }
}
