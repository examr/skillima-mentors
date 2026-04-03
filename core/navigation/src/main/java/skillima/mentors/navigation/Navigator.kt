package skillima.mentors.navigation

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import skillima.data.local.repository.LocalAppDataRepository
import skillima.data.local.model.AppDataConfig

/**
 * Manages the navigation back stack and navigation logic for the entire application.
 *
 * This class is the single source of truth for the current screen and the history of screens
 * the user has visited. It observes user data changes (like login status) from a [DatastoreHelper]
 * to automatically navigate the user to the correct destination, such as the login screen
 * or the home screen.
 *
 * It provides methods to navigate to new destinations (`goTo`), go back (`goBack`), and handle
 * specific app state transitions like completing onboarding, logging in, and logging out,
 * which often involve clearing the back stack to create a logical user flow.
 *
 * The navigation logic is reactive. It's initialized with a splash screen and then determines
 * the appropriate starting destination based on the user's persisted data (e.g., `isFirstTime`, `isLoggedIn`).
 *
 * @param userRepository The repository providing access to user session data, like login status and onboarding completion.
 * @param scope A [CoroutineScope] used to launch a coroutine for collecting the user data flow,
 *              ensuring the navigation state is always in sync with the user's session data.
 */// Module: :navigation
class Navigator(
    private val userRepository: LocalAppDataRepository,
    private val scope: CoroutineScope, // We will inject a scope for flow collection
) {
    // Start with Splash so the user sees nothing while we check DataStore
    val backStack = mutableStateListOf<Destinations>(OnboardingScreen)

    var isLoggedIn by mutableStateOf(false)
    var isFirstTime by mutableStateOf(false)

    val loginDestination: Destinations = LoginScreen

    init {
        // Collect the flow immediately upon creation
        scope.launch {
            userRepository.getAppData.collect { data ->
                var startDestination = determineDestination(data)
                isLoggedIn = data.loggedIn
                isFirstTime = data.firstTime
                // Only update if the destination is different to avoid loops

                if (backStack.lastOrNull() != startDestination) {
                    backStack.clear()
                    backStack.add(startDestination)
                }

            }
        }
    }
    // replaces the top of backstack and prevents multiple entries of same screen in backstack
    fun replaceTop(destination: Destinations) {
        if (backStack.lastOrNull() == destination) return

        if (backStack.isNotEmpty()) {
            backStack.removeAt(backStack.lastIndex)
        }
        backStack.add(destination)
    }


    // Your logic from MainActivity moves here
//    private fun determineDestination(data: AppDataConfig): Destinations {
//        return when {
//            data.firstTime && !data.loggedIn -> OnboardingScreen
//            !data.loggedIn -> WelcomeScreen
//            else -> HomeScreen
//        }
//    }

    private fun determineDestination(data: AppDataConfig): Destinations {
        return when {
            data.loggedIn -> GuildScreen
            data.firstTime -> OnboardingScreen
            else -> SignupScreen
        }
    }

    // Tracks where the user WANTED to go before we intercepteS
    private var pendingDestination: Destinations? = null

    // --- Actions ---

    fun goTo(destination: Destinations) {
        // Guard Clause: Check for Login Requirement
        if (destination is RequiresLogin && !isLoggedIn) {
            pendingDestination = destination
            backStack.add(loginDestination)
        } else {
            backStack.add(destination)
        }
    }

    fun goBack() {
        if (backStack.size > 1) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            backStack.removeLast()
        } else {
            backStack.removeAt(backStack.lastIndex)
        }
    }

}