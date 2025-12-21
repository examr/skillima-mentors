package skillima.screens.onboarding.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.getKoin
import skillima.mentors.navigation.LoginScreen
import skillima.mentors.navigation.Navigator
import skillima.mentors.navigation.SignupScreen
import skillima.screens.onboarding.WelcomeScreen

@Composable
fun WelcomeRoute(navigator: Navigator = getKoin().get()) {
    WelcomeScreen(navigateToLogin = {
        navigator.goTo(LoginScreen)
    }, navigateToSignup = {
        navigator.goTo(SignupScreen)
    })

}