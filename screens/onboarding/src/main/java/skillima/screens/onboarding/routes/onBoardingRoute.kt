package skillima.screens.onboarding.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import skillima.data.local.LocalAppDataRepository
import skillima.mentors.navigation.Navigator
import skillima.mentors.navigation.OnboardingScreen
import skillima.mentors.navigation.WelcomeScreen
import skillima.screens.onboarding.OnBoardingScreen
import skillima.screens.onboarding.OnboardingViewModel

@Composable
fun OnBoardingRoute(navigator: Navigator = getKoin().get()) {

    val viewModel = koinViewModel<OnboardingViewModel>()


    OnBoardingScreen {
        viewModel.setOnboardingComplete()
        navigator.goTo(WelcomeScreen)
        navigator.backStack.remove(OnboardingScreen)
    }

}