package skillima.data.profile.di

import org.koin.dsl.module
import skillima.data.profile.repository.ProfileRepository
import skillima.data.profile.repository.ProfileRepositoryImpl

val profileDataModule = module {
    single<ProfileRepository> {
        ProfileRepositoryImpl(get(), get())
    }
}
