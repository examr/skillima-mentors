package skillima.mentors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import skillima.data.local.repository.LocalAppDataRepository
import skillima.data.local.model.AppDataConfig

class MainViewModel(
    private val localAppDataRepository: LocalAppDataRepository
) : ViewModel() {

    val mainUIState = localAppDataRepository.getAppData.map {
        MainActivityState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MainActivityState.Loading
    )
}

val mainViewModel = module {
    viewModel { MainViewModel(get()) }
}

sealed interface MainActivityState {
    data object Loading : MainActivityState
    data class Success(
        val userData: AppDataConfig,
    ) : MainActivityState
}