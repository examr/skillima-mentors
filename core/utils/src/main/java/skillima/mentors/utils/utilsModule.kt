package skillima.mentors.utils

import org.koin.dsl.module

val utilsModule = module {
    single { NotificationObserver(get()) }
    single { ConnectivityMonitor(get()) }
}