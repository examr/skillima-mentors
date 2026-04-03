package skillima.data.auth.di

import org.koin.dsl.module
import skillima.data.auth.repository.AuthRepository
import skillima.data.auth.repository.AuthRepositoryImpl

val authDataModule  = module{
    single<AuthRepository> {
        AuthRepositoryImpl(get(),get())
    }


}