package skillima.screens.mentorprofile

import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import skillima.mentors.navigation.MentorProfileScreen

@OptIn(KoinExperimentalAPI::class)
val mentorProfileModule = module {
    activityRetainedScope {
        navigation<MentorProfileScreen> {
            MentorProfileRoute()
        }
    }

    single {
        MentorProfileViewModel(get(), get())
    }
}
