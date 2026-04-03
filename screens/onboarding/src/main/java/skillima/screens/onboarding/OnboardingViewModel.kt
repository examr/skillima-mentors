package skillima.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import skillima.data.local.repository.LocalAppDataRepository

class OnboardingViewModel(
    private val localAppDataRepository: LocalAppDataRepository
) : ViewModel() {

    fun setOnboardingComplete() {
        viewModelScope.launch {
            localAppDataRepository.setOnboardingComplete("GUEST")
        }
    }
}

