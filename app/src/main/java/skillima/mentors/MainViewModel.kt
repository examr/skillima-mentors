package skillima.mentors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import skillima.data.local.model.AppDataConfig
import skillima.data.local.repository.local.LocalAppDataRepository
import skillima.data.local.repository.user.UserLocalRepository
import skillima.mentors.supabase.SupabaseConstants

class MainViewModel(
    private val localAppDataRepository: LocalAppDataRepository,
    private val userLocalRepository: UserLocalRepository,
    private val supabaseClient: SupabaseClient,
) : ViewModel() {

    private val bootstrapComplete = MutableStateFlow(false)

    val mainUIState = combine(
        localAppDataRepository.getAppData,
        bootstrapComplete,
    ) { appData, isBootstrapped ->
        if (isBootstrapped) {
            MainActivityState.Success(appData)
        } else {
            MainActivityState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MainActivityState.Loading
    )

    init {
        bootstrapSessionState()
    }

    private fun bootstrapSessionState() {
        viewModelScope.launch {
            val appData = localAppDataRepository.getAppData.first()
            if (!appData.loggedIn) {
                bootstrapComplete.value = true
                return@launch
            }

            syncProfileCompletionFromLocal()
            syncGuildSelectionFromRemote(appData.isGuildSelected)
            bootstrapComplete.value = true
        }
    }

    private suspend fun syncProfileCompletionFromLocal() {
        val user = userLocalRepository.observeUser().first() ?: return
        val isProfileComplete = user.bio.isNotBlank() &&
            user.githubUrl.isNotBlank() &&
            user.linkedinUrl.isNotBlank() &&
            user.xUrl.isNotBlank()

        localAppDataRepository.setProfileComplete(isProfileComplete)
    }

    private suspend fun syncGuildSelectionFromRemote(isGuildSelected: Boolean) {
        // Only check remote if local flag says not yet selected
        if (isGuildSelected) return

        val userId = userLocalRepository.getUserId() ?: return

        val hasSkills = runCatching {
            val result = supabaseClient.from(SupabaseConstants.USER_SKILLS)
                .select() {
                    filter { eq("user_id", userId) }
                }
            (result.countOrNull() ?: 0L) > 0L
        }.getOrDefault(false)

        if (hasSkills) {
            localAppDataRepository.setGuildSelectionComplete(true)
        }
    }
}

val mainViewModel = module {
    viewModel { MainViewModel(get(), get(), get()) }
}

sealed interface MainActivityState {
    data object Loading : MainActivityState
    data class Success(
        val userData: AppDataConfig,
    ) : MainActivityState
}
