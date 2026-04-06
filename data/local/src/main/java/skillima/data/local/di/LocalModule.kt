package skillima.data.local.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import skillima.data.local.db.AppDatabase
import skillima.data.local.repository.local.LocalAppDataRepository
import skillima.data.local.repository.local.LocalAppDataRepositoryImpl
import skillima.data.local.repository.skills.UserSkillLocalRepository
import skillima.data.local.repository.skills.UserSkillLocalRepositoryImpl
import skillima.data.local.repository.user.UserLocalRepository
import skillima.data.local.repository.user.UserLocalRepositoryImpl
import skillima.data.local.utils.AppDatabaseMigrations

val offlineRepositories = module {
    single<LocalAppDataRepository> { LocalAppDataRepositoryImpl(get(), get()) }
}


val localDatabaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "skillima.db"
        )
            .addMigrations(*AppDatabaseMigrations.ALL)
            .build()
    }
    single {
        get<AppDatabase>().userDao()

    }
    single { get<AppDatabase>().userSkillDao() }

    single<UserLocalRepository> {
        UserLocalRepositoryImpl(
            userDao = get()
        )
    }



    single<UserSkillLocalRepository> {
        UserSkillLocalRepositoryImpl(get())
    }


}
