package skillima.screens.auth.state

import skillima.mentors.module.AuthUser

sealed class SignupUiState {

    object Idle: SignupUiState()
    object Loading : SignupUiState()
    data class Success(val message: AuthUser, val hasSkills: Boolean = false, val isProfileComplete: Boolean = false) : SignupUiState()

    data class Error(val errorMessage: Int) : SignupUiState()
}
