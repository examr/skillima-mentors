package skillima.screens.auth.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.getKoin
import skillima.mentors.navigation.Navigator
import skillima.mentors.navigation.SignupScreen
import skillima.screens.auth.AuthViewmodel
import skillima.screens.auth.screens.LoginScreen
import  org.koin.androidx.compose.koinViewModel
@Composable
fun LoginRoute(
    viewModel: AuthViewmodel = koinViewModel(),
    navigator: Navigator = getKoin().get()
) {
    val loginUiState by viewModel.loginUiState.collectAsState()
    val userInput by viewModel.userInput.collectAsState()

    LoginScreen(
        loginUiState = loginUiState,
        userInput = userInput,
        onEvent = viewModel::onEvent,
        navigateToSignup = {
            navigator.replaceTop(SignupScreen)
        }
    )
}
