package skillima.screens.home

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val name: String, val email: String) : HomeUiState
    data object Error : HomeUiState
}

sealed interface LogoutState {
    data object Idle : LogoutState
    data object Loading : LogoutState
    data object Done : LogoutState
}
