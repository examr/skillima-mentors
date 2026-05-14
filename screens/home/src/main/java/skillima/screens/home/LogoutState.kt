package skillima.screens.home

sealed interface LogoutState {
    data object Idle : LogoutState
    data object Loading : LogoutState
    data object Done : LogoutState
}
