package skillima.screens.auth

import android.provider.Settings.Global.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import skillima.core.module.UserData
import skillima.core.utils.Response
import skillima.data.auth.error.AuthError
import skillima.data.auth.repository.AuthRepository
import skillima.screens.auth.state.AuthEvents
import skillima.screens.auth.state.LoginUiState
import skillima.screens.auth.state.UserInput


class AuthViewmodel(private val authRepository: AuthRepository) : ViewModel() {
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow()

    private val _userInputState = MutableStateFlow<UserInput>(UserInput())
    val userInput = _userInputState.asStateFlow()

    fun onEvent(authEvents: AuthEvents) {
        when (authEvents) {
            is AuthEvents.OnEmailChange -> {
                _userInputState.value = _userInputState.value.updateEmail(authEvents.email)
            }

            is AuthEvents.OnPasswordChange -> {
                _userInputState.value = _userInputState.value.updatePassword(authEvents.password)

            }

            is AuthEvents.Login -> {login(authEvents.userData)}
        }
    }

    fun login(userData: UserData) {
        viewModelScope.launch {
            authRepository.login(userData).collect { res ->
                when (res) {
                    is Response.Loading -> {
                        _loginUiState.value = LoginUiState.Loading
                    }
                    is Response.Error -> {
                        _loginUiState.value = LoginUiState.Error(res.exception.error)
                    }
                    is Response.Success-> {
                        // Cast if needed, or pass the payload to Success state
                        _loginUiState.value = LoginUiState.Success(res.data)
                    }
                }
            }
        }
    }


}