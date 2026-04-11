package skillima.screens.home

import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import skillima.mentors.navigation.HomeScreen

@OptIn(KoinExperimentalAPI::class)
val homeModule = module {
    activityRetainedScope {
        navigation<HomeScreen> {
            HomeRoute()
        }
    }

    single {
        HomeViewModel(get(), get())
    }
}
