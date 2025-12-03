package skillima.core.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object Onboarding : Screen()
    @Serializable
    data object WelcomeScreen:Screen()
    @Serializable
    data object LoginScreen:Screen()
    @Serializable
    data object SignupScreen:Screen()

}
