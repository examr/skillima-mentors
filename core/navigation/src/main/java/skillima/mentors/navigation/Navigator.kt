package skillima.mentors.navigation

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import skillima.data.local.model.AppDataConfig

class Navigator {
    // Starts empty — nothing renders until initialize() is called
    val backStack = mutableStateListOf<Destinations>()

    var isReady by mutableStateOf(false)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    private var initialized = false

    val loginDestination: Destinations = LoginScreen

    fun initialize(data: AppDataConfig) {
        if (initialized) return
        initialized = true
        isLoggedIn = data.loggedIn
        backStack.add(determineDestination(data))
        isReady = true
    }

    private fun determineDestination(data: AppDataConfig): Destinations = when {
        data.loggedIn -> GuildScreen  // TODO: swap to HomeScreen once implemented
        data.firstTime -> OnboardingScreen
        else -> LoginScreen
    }

    fun replaceTop(destination: Destinations) {
        if (backStack.lastOrNull() == destination) return
        if (backStack.isNotEmpty()) backStack.removeAt(backStack.lastIndex)
        backStack.add(destination)
    }

    private var pendingDestination: Destinations? = null

    fun goTo(destination: Destinations) {
        if (destination is RequiresLogin && !isLoggedIn) {
            pendingDestination = destination
            backStack.add(loginDestination)
        } else {
            backStack.add(destination)
        }
    }

    fun goBack() {
        if (backStack.size > 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                backStack.removeLast()
            } else {
                backStack.removeAt(backStack.lastIndex)
            }
        }
    }
}
