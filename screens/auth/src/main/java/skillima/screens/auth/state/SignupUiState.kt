package skillima.screens.auth.state

sealed class SignupUiState {

    object Idle: SignupUiState()
    object Loading : SignupUiState()
    data class Success(val message: Boolean) : SignupUiState()

    data class Error(val errorMessage: Int) : SignupUiState()
}
