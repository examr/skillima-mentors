package skillima.screens.mentorprofile

sealed interface MentorProfileUiState {
    data object Idle : MentorProfileUiState
    data object Loading : MentorProfileUiState
    data object Success : MentorProfileUiState
    data class Error(val message: String) : MentorProfileUiState
}
