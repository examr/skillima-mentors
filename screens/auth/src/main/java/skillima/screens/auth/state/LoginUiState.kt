package skillima.screens.auth.state

import skillima.mentors.module.AuthUser
import skillima.mentors.module.UserData

sealed class LoginUiState {

    object Idle: LoginUiState()
    object Loading : LoginUiState()
    data class Success(val message: AuthUser, val hasSkills: Boolean = false, val isProfileComplete: Boolean = false) : LoginUiState()

    data class Error(val errorMessage: Int) : LoginUiState()
}
