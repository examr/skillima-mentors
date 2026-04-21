package skillima.screens.onboarding.routes

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import skillima.mentors.navigation.LoginScreen
import skillima.mentors.navigation.Navigator
import skillima.mentors.navigation.OnboardingScreen
import skillima.mentors.navigation.SignupScreen
import skillima.screens.onboarding.OnBoardingScreen
import skillima.screens.onboarding.OnboardingViewModel

@Composable
fun OnBoardingRoute(navigator: Navigator = getKoin().get()) {
    val viewModel = koinViewModel<OnboardingViewModel>()

    OnBoardingScreen(
        onLoginClick = {
          //  viewModel.setOnboardingComplete()
            navigator.goTo(LoginScreen)
            navigator.backStack.remove(OnboardingScreen)
        },
        onSignupClick = {
          //  viewModel.setOnboardingComplete()
            navigator.goTo(SignupScreen)
            navigator.backStack.remove(OnboardingScreen)
        }
    )
}