package skillima.screens.mentorprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun MentorProfileRoute() {
    val viewModel: MentorProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // When save succeeds, DataStore update triggers Navigator to re-route to GuildScreen automatically
    MentorProfileScreen(
        uiState = uiState,
        onSave = { bio, githubUrl, linkedinUrl, xUrl ->
            viewModel.saveProfile(bio, githubUrl, linkedinUrl, xUrl)
        }
    )
}
