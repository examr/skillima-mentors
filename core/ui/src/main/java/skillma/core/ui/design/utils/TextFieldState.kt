package skillma.core.ui.design.utils

import androidx.compose.runtime.Stable

@Stable
interface TextFieldState {
    data object Loading : TextFieldState
    data object Error : TextFieldState
    data object Success : TextFieldState
    data object Idle : TextFieldState
}