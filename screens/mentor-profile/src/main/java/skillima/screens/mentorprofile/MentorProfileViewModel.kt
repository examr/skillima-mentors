package skillima.screens.mentorprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import skillima.data.local.repository.local.LocalAppDataRepository

class MentorProfileViewModel(
    private val supabaseClient: SupabaseClient,
    private val localAppDataRepository: LocalAppDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MentorProfileUiState>(MentorProfileUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun saveProfile(
        bio: String,
        githubUrl: String,
        linkedinUrl: String,
        xUrl: String,
    ) {
        if (bio.isBlank()) {
            _uiState.value = MentorProfileUiState.Error("Bio is required")
            return
        }
        viewModelScope.launch {
            _uiState.value = MentorProfileUiState.Loading
            runCatching {
                val userId = supabaseClient.auth.currentUserOrNull()?.id
                    ?: throw IllegalStateException("Not authenticated")
                supabaseClient.from("profiles").upsert(
                    buildJsonObject {
                        put("id", userId)
                        put("bio", bio)
                        if (githubUrl.isNotBlank()) put("github_url", githubUrl)
                        if (linkedinUrl.isNotBlank()) put("linkedin_url", linkedinUrl)
                        if (xUrl.isNotBlank()) put("x_url", xUrl)
                    }
                )
            }.fold(
                onSuccess = {
                    localAppDataRepository.setProfileComplete(true)
                    _uiState.value = MentorProfileUiState.Success
                },
                onFailure = { e ->
                    _uiState.value = MentorProfileUiState.Error(e.message ?: "Failed to save profile")
                }
            )
        }
    }
}
