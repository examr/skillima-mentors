package skillima.mentors

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.compose.navigation3.getEntryProvider
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.scope.Scope
import skillima.mentors.navigation.Navigator
import skillma.core.ui.theme.SkillimaMentorsTheme

class MainActivity : ComponentActivity(), AndroidScopeComponent {
    val mainViewModel: MainViewModel by inject<MainViewModel>()
    val navigator: Navigator by inject()

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* granted */ }

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Request POST_NOTIFICATIONS on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        var uiState: MainActivityState by mutableStateOf(MainActivityState.Loading)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.mainUIState.onEach { state ->
                    if (state is MainActivityState.Success) {
                        navigator.initialize(state.userData)
                    }
                    uiState = state
                }.collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            uiState is MainActivityState.Loading
        }


        enableEdgeToEdge()
        setContent {
            SkillimaMentorsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (navigator.isReady) {
                        NavDisplay(
                            backStack = navigator.backStack,
                            modifier = Modifier.padding(innerPadding),
                            onBack = { navigator.goBack() },
                            entryProvider = getEntryProvider(),
                        )
                    }
                }
            }
        }
    }


    override val scope: Scope by activityRetainedScope()
}

