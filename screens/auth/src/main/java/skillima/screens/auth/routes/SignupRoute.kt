package skillima.screens.auth.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import skillima.mentors.navigation.LoginScreen
import skillima.mentors.navigation.MentorProfileScreen
import skillima.mentors.navigation.Navigator
import skillima.screens.auth.AuthViewmodel
import skillima.screens.auth.screens.SignupScreen

@Composable
fun SignupRoute(
    viewModel: AuthViewmodel = koinViewModel(),
    navigator: Navigator = getKoin().get(),
) {
    val signupUiState by viewModel.signupUiState.collectAsState()
    val userInput by viewModel.userInput.collectAsState()
    SignupScreen(
        signupUiState = signupUiState,
        userInput = userInput,
        onEvent = viewModel::onEvent,
        navigateToLogin = {
            navigator.replaceTop(LoginScreen)
        },
        onSuccess = { isProfileComplete ->
            navigator.backStack.clear()
            if (!isProfileComplete) {
                // Mentors must complete their profile before selecting a guild
                navigator.goTo(MentorProfileScreen)
            } else {
                navigator.goTo(skillima.mentors.navigation.GuildScreen)
            }
        }
    )

}