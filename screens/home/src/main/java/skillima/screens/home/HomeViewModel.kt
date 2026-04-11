package skillima.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive
import skillima.data.local.repository.local.LocalAppDataRepository

class HomeViewModel(
    private val supabaseClient: SupabaseClient,
    private val localAppDataRepository: LocalAppDataRepository,
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
            val user = supabaseClient.auth.currentUserOrNull()
            if (user != null) {
                val name = user.userMetadata?.get("name")?.jsonPrimitive?.content ?: ""
                val email = user.email ?: ""
                _uiState.value = HomeUiState.Success(name = name, email = email)
            } else {
                _uiState.value = HomeUiState.Error
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
