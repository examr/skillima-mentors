package skillima.core.auth.state

sealed class LoginUiState {
    object Idle: LoginUiState()
    object Loading : LoginUiState()
    data class Success(val message: Boolean) : LoginUiState()

    data class Error(val errorMessage: String) : LoginUiState()
}
