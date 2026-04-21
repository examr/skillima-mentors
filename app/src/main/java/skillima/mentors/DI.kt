package skillima.mentors

import org.koin.dsl.module
import skillima.data.auth.di.authDataModule
import skillima.data.guild.di.guildDataModule
import skillima.data.local.di.localDatabaseModule
import skillima.data.local.di.offlineRepositories
import skillima.mentors.datastore.datastoreModule
import skillima.mentors.navigation.Navigator
import skillima.mentors.supabase.di.supabaseModule
import skillima.mentors.utils.utilsModule
import skillima.screens.auth.authPresentation
import skillima.screens.guild.guildPresentation
import skillima.screens.home.homeModule
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
        supabaseModule,
        localDatabaseModule,
        guildPresentation,
        guildDataModule,
        homeModule




    )

    single { Navigator() }
}
