package skillima.screens.onboarding

import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import skillima.mentors.navigation.OnboardingScreen
import skillima.mentors.navigation.WelcomeScreen
import skillima.screens.onboarding.routes.OnBoardingRoute
import skillima.screens.onboarding.routes.WelcomeRoute

@OptIn(KoinExperimentalAPI::class)
val onboardingModule = module {
    activityRetainedScope {
        navigation<OnboardingScreen> { OnBoardingRoute() }
    }
    activityRetainedScope {
        navigation<WelcomeScreen> {
            WelcomeRoute()
        }
    }


    single { OnboardingViewModel(get()) }
}