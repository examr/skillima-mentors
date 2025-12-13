package skillima.screens.auth.state

sealed class LoginUiState {

    object Idle: LoginUiState()
    object Loading : LoginUiState()
    data class Success(val message: Boolean) : LoginUiState()

    data class Error(val errorMessage: Int) : LoginUiState()
}
