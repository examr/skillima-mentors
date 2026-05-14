package skillima.core.notifications.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import skillima.core.notifications.repository.NotificationTokenRepository
import skillima.core.notifications.repository.NotificationTokenRepositoryImpl

val notificationsModule = module {
    single<NotificationTokenRepository> {
        NotificationTokenRepositoryImpl(androidContext())
    }
}
