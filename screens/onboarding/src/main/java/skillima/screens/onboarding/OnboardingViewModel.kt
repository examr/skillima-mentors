package skillima.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import skillima.data.local.LocalAppDataRepository

class OnboardingViewModel(
    private val localAppDataRepository: LocalAppDataRepository
) : ViewModel() {

    fun setOnboardingComplete() {
        viewModelScope.launch {
            localAppDataRepository.setOnboardingComplete("GUEST")
        }
    }
}

