package skillima.screens.auth.state

import skillima.mentors.module.UserData

sealed class SignupUiState {

    object Idle: SignupUiState()
    object Loading : SignupUiState()
    data class Success(val message: UserData) : SignupUiState()

    data class Error(val errorMessage: Int) : SignupUiState()
}
