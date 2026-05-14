package skillima.screens.home

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val name: String, val email: String) : HomeUiState
    data class AwaitingVerification(val name: String) : HomeUiState
    data object Error : HomeUiState
}
