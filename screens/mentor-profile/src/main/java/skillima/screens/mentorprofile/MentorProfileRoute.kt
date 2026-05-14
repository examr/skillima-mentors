package skillima.screens.mentorprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import skillima.mentors.navigation.GuildScreen
import skillima.mentors.navigation.Navigator

@Composable
fun MentorProfileRoute(
    navigator: Navigator = getKoin().get(),
) {
    val viewModel: MentorProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is MentorProfileUiState.Success) {
            navigator.backStack.clear()
            navigator.goTo(GuildScreen)
        }
    }

    MentorProfileScreen(
        uiState = uiState,
        onSave = { bio, githubUrl, linkedinUrl, xUrl ->
            viewModel.saveProfile(bio, githubUrl, linkedinUrl, xUrl)
        }
    )
}
