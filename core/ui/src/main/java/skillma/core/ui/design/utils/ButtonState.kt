package skillma.core.ui.design.utils

import androidx.compose.runtime.Stable

@Stable
sealed interface ButtonState {
    data object Idle : ButtonState
    data object Loading : ButtonState
    data object Success : ButtonState
}