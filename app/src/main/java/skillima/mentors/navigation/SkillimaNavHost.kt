package skillima.mentors.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.koin.androidx.compose.koinViewModel
import skillima.screens.auth.AuthViewmodel
import skillima.screens.auth.screens.LoginScreen
import skillima.screens.auth.screens.SignupScreen
import skillima.screens.onboarding.OnBoardingScreen
import skillima.screens.onboarding.WelcomeScreen


@Composable
fun SkillimaNavHost(modifier: Modifier = Modifier) {
    val backstack = remember { mutableStateListOf<Screen>(Screen.Onboarding) }
    val authViewmodel: AuthViewmodel = koinViewModel()
    val userInput by authViewmodel.userInput.collectAsState()
    val loginUiState by authViewmodel.loginUiState.collectAsState()
    val signupUiState by authViewmodel.signupUiState.collectAsState()
    NavDisplay(
        backStack = backstack,
        entryProvider = entryProvider {
            entry<Screen.Onboarding> {
                OnBoardingScreen(
                    onFinished = {
                        backstack.clear()
                        backstack += Screen.WelcomeScreen
                    }
                )
            }
            entry<Screen.WelcomeScreen> {
                WelcomeScreen(
                    navigateToLogin = {
                        backstack.clear()
                        backstack += Screen.LoginScreen
                    },
                    navigateToSignup = {
                        backstack.clear()
                        backstack += Screen.SignupScreen
                    }
                )
            }
            entry<Screen.LoginScreen> {
                LoginScreen(
                    userInput = userInput,
                    onEvent = authViewmodel::onEvent,
                    loginUiState = loginUiState,
                    navigateToSignup = {
                        backstack.clear()
                        backstack += Screen.LoginScreen
                    }
                )
            }
            entry<Screen.SignupScreen> {
                SignupScreen(
                    userInput = userInput,
                    onEvent = authViewmodel::onEvent,
                    signupUiState = signupUiState,
                     navigateToLogin = {
                         backstack.clear()
                         backstack += Screen.LoginScreen
                     }

                )
            }
        })
}