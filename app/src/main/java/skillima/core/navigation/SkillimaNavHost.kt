package skillima.core.navigation

import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import skillima.core.auth.screens.LoginScreen
import skillima.core.onboarding.OnBoardingScreen
import skillima.core.onboarding.WelcomeScreen


@Composable
fun SkillimaNavHost(modifier: Modifier = Modifier) {
    val backstack = remember { mutableStateListOf<Screen>(Screen.LoginScreen) }
    NavDisplay(backStack = backstack,
        entryProvider = entryProvider {
            entry<Screen.Onboarding> {
                OnBoardingScreen(
                    onFinished = {
                        backstack.clear()
                        backstack += Screen.WelcomeScreen
                    }
                )
            }
            entry<Screen.WelcomeScreen>{
                WelcomeScreen(
                    navigateToLogin = {
                        backstack.clear()
                        backstack += Screen.LoginScreen
                    },
                    navigateToSignup = {
                        backstack.clear()
                        backstack += Screen.LoginScreen
                    }
                )
            }
            entry<Screen.LoginScreen> {
                LoginScreen()
            }
        })
}