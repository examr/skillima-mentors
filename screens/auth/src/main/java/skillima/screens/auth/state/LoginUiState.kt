package skillima.screens.auth.state

import skillima.mentors.module.UserData

sealed class LoginUiState {

    object Idle: LoginUiState()
    object Loading : LoginUiState()
    data class Success(val message: UserData) : LoginUiState()

    data class Error(val errorMessage: Int) : LoginUiState()
}
