package skillima.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import skillima.data.local.repository.local.LocalAppDataRepository
import skillima.data.profile.repository.ProfileRepository
import skillima.mentors.utils.Response

class HomeViewModel(
    private val supabaseClient: SupabaseClient,
    private val localAppDataRepository: LocalAppDataRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _logoutState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    val logoutState = _logoutState.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _uiState.value = when (val result = profileRepository.getCurrentMentorProfile()) {
                is Response.Success -> {
                    if (result.data.verificationStatus == "pending") {
                        HomeUiState.AwaitingVerification(result.data.name)
                    } else {
                        HomeUiState.Success(
                            name = result.data.name,
                            email = result.data.email,
                        )
                    }
                }

                else -> HomeUiState.Error
            }
        }
    }

    fun logout() {
        if (_logoutState.value is LogoutState.Loading) return
        viewModelScope.launch {
            _logoutState.value = LogoutState.Loading
            try {
                supabaseClient.auth.signOut()
            } catch (e: Exception) {
                // Proceed with local logout even if remote sign-out fails
            }
            localAppDataRepository.setLoggedIn(false)
            _logoutState.value = LogoutState.Done
        }
    }
}
