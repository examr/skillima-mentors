package skillima.screens.auth.di

import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import skillima.screens.auth.AuthViewmodel

val authPresentationModule = module {
    // Koin will resolve AuthRepository from dataModule (get())
    viewModel {
        AuthViewmodel(get())
    }
}
