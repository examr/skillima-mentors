package skillima.data.local

import org.koin.dsl.module

val offlineRepositories = module {
    single<LocalAppDataRepository> { LocalAppDataRepositoryImpl(get(),get()) }
}