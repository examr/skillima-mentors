package skillima.screens.mentorprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import skillima.data.local.repository.local.LocalAppDataRepository
import skillima.data.local.repository.user.UserLocalRepository
import skillima.mentors.supabase.SupabaseConstants

class MentorProfileViewModel(
    private val supabaseClient: SupabaseClient,
    private val localAppDataRepository: LocalAppDataRepository,
    private val userLocalRepository: UserLocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MentorProfileUiState>(MentorProfileUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun saveProfile(
        bio: String,
        githubUrl: String,
        linkedinUrl: String,
        xUrl: String,
    ) {
        if (
            bio.isBlank() ||
            githubUrl.isBlank() ||
            linkedinUrl.isBlank() ||
            xUrl.isBlank()
        ) {
            _uiState.value = MentorProfileUiState.Error(
                "Complete your profile with bio, GitHub, LinkedIn, and X URL"
            )
            return
        }
        viewModelScope.launch {
            _uiState.value = MentorProfileUiState.Loading
            runCatching {
                val userId = userLocalRepository.getUserId()
                    ?: throw IllegalStateException("Not authenticated")

                // Update profiles — only columns that exist in schema:
                // profiles has: bio, github_url, linkedin_url, x_url (NO portfolio_url)
                supabaseClient.postgrest[SupabaseConstants.PROFILE].update(
                    {
                        set("bio", bio)
                        if (githubUrl.isNotBlank()) set("github_url", githubUrl)
                        if (linkedinUrl.isNotBlank()) set("linkedin_url", linkedinUrl)
                        if (xUrl.isNotBlank()) set("x_url", xUrl)
                    }
                ) {
                    filter {
                        eq("id", userId)
                    }
                }

                // Ensure mentor_profiles row exists with pending status.
                // mentor_profiles has NO github_username (student_profiles only) and NO portfolio_url.
                supabaseClient.postgrest[SupabaseConstants.MENTOR_PROFILES].upsert(
                    buildJsonObject {
                        put("user_id", userId)
                        put("verification_status", "pending")
                    }
                ) {
                    onConflict = "user_id"
                }

                val currentUser = userLocalRepository.observeUser().first()
                    ?: throw IllegalStateException("Local user not found")

                userLocalRepository.saveUser(
                    currentUser.copy(
                        bio = bio,
                        githubUrl = githubUrl,
                        linkedinUrl = linkedinUrl,
                        xUrl = xUrl,
                    )
                )
            }.fold(
                onSuccess = {
                    viewModelScope.launch {
                        localAppDataRepository.setProfileComplete(true)
                    }
                    _uiState.value = MentorProfileUiState.Success
                },
                onFailure = { e ->
                    _uiState.value = MentorProfileUiState.Error(e.message ?: "Failed to save profile")
                }
            )
        }
    }
}
