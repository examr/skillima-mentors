package skillima.screens.auth

import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import skillima.mentors.navigation.LoginScreen
import skillima.mentors.navigation.SignupScreen
import skillima.screens.auth.routes.LoginRoute
import skillima.screens.auth.routes.SignupRoute

@OptIn(KoinExperimentalAPI::class)
val authPresentation = module {
    activityRetainedScope {
        navigation<LoginScreen> { LoginRoute()}
    }

    activityRetainedScope {
        navigation <SignupScreen>{ SignupRoute() }
    }

    // ViewModel DI
    single {
        AuthViewmodel(get(),get())
    }
}
