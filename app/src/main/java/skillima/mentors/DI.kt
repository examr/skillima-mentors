package skillima.mentors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module
import skillima.data.auth.di.authDataModule
import skillima.data.local.offlineRepositories
import skillima.mentors.datastore.datastoreModule
import skillima.mentors.navigation.Navigator
import skillima.mentors.supabase.di.supabaseModule
import skillima.mentors.utils.utilsModule
import skillima.screens.auth.authPresentation
import skillima.screens.onboarding.onboardingModule

val appModules = module {
    includes(
        authDataModule,
        authPresentation,
        datastoreModule,
        utilsModule,
        offlineRepositories,
        mainViewModel,
        onboardingModule,
        supabaseModule


    )

    // Coroutine scope for navigation
    factory(named("NavScope")) {
        CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    }

    single {
        Navigator(
            get(),
            get(named("NavScope"))
        )
    }
}
