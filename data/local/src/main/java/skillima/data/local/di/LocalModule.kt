package skillima.data.local.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import skillima.data.local.db.AppDatabase
import skillima.data.local.repository.LocalAppDataRepository
import skillima.data.local.repository.LocalAppDataRepositoryImpl
import skillima.data.local.repository.UserLocalRepository
import skillima.data.local.repository.UserLocalRepositoryImpl

val offlineRepositories = module {
    single<LocalAppDataRepository> { LocalAppDataRepositoryImpl(get(), get()) }
}


val localDatabaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "skillima.db"
        ).build()
    }

    single {
        get<AppDatabase>().userDao()
    }
    single<UserLocalRepository> {
        UserLocalRepositoryImpl(
            userDao = get()
        )
    }

}
