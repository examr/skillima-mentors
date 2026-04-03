package skillima.screens.guild

import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import skillima.mentors.navigation.GuildScreen
import skillima.screens.guild.routes.GuildScreen

@OptIn(KoinExperimentalAPI::class)
val guildPresentation = module {
    activityRetainedScope {
        navigation<GuildScreen> {
            GuildScreen()
        }
    }

    single {
        GuildViewModel(get(),get())
    }


}