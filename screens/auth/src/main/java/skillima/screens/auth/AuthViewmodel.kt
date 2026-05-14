package skillima.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import skillima.mentors.module.UserData
import skillima.mentors.utils.Response
import skillima.mentors.utils.validator.EmailValidator
import skillima.mentors.utils.validator.PasswordStrength
import skillima.mentors.utils.validator.PasswordValidator
import skillima.data.auth.repository.AuthRepository
import skillima.data.local.mapper.toEntity
import skillima.data.local.repository.user.UserLocalRepository
import skillima.screens.auth.state.AuthEvents
import skillima.screens.auth.state.LoginUiState
import skillima.screens.auth.state.SignupUiState
import skillima.screens.auth.state.UserInput
class AuthViewmodel(
    private val authRepository: AuthRepository,
    private val userLocalRepository: UserLocalRepository

) : ViewModel() {

    private val _loginUiState =
        MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow()

    private val _signupUiState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val signupUiState = _signupUiState.asStateFlow()

    private val _userInputState =
        MutableStateFlow(UserInput())
    val userInput = _userInputState.asStateFlow()

    init {
        observeDebouncedValidation()
    }


    fun onEvent(authEvents: AuthEvents) {

        when (authEvents) {

            is AuthEvents.OnNameChange -> {
                _userInputState.value =
                    _userInputState.value.copy(name = authEvents.name)
            }

            is AuthEvents.OnEmailChange -> {
                _userInputState.value =
                    _userInputState.value.copy(email = authEvents.email)
            }

            is AuthEvents.OnPasswordChange -> {
                _userInputState.value =
                    _userInputState.value.copy(password = authEvents.password)
            }

            is AuthEvents.Login -> {
                login(authEvents.userData)
            }

            is AuthEvents.Signup ->{
                signup(userData = authEvents.userData)
            }
        }
    }


    private fun observeDebouncedValidation() {

        // NAME
        viewModelScope.launch {
            userInput
                .map { it.name }
                .distinctUntilChanged()
                .debounce(500)
                .collect { name ->
                    _userInputState.value =
                        _userInputState.value
                            .copy(
                                isNameValid = name.length >= 5,
                                nameInteracted = name.isNotEmpty()
                            )
                }
        }

        // EMAIL
        viewModelScope.launch {
            userInput
                .map { it.email }
                .distinctUntilChanged()
                .debounce(600)
                .collect { email ->
                    _userInputState.value =
                        _userInputState.value
                            .copy(
                                isEmailValid = EmailValidator.validateEmail(email),
                                emailInteracted = email.isNotEmpty()
                            )
                }
        }

        // PASSWORD
        viewModelScope.launch {
            userInput
                .map { it.password }
                .distinctUntilChanged()
                .debounce(700)
                .collect { password ->

                    val (rules, strength) =
                        PasswordValidator.isStrongPassword(
                            password,
                            _userInputState.value.email,
                            _userInputState.value.name
                        )

                    val isValid =
                        strength == PasswordStrength.STRONG &&
                                rules.values.all { it }

                    _userInputState.value =
                        _userInputState.value.applyPasswordValidation(
                            isValid = isValid,
                            strength = strength,
                            rules = rules
                        )
                }
        }


    }

    private fun login(userData: UserData) {
        viewModelScope.launch {
            authRepository.login(userData).collect { res ->
                when (res) {

                    is Response.Loading -> {
                        _loginUiState.value = LoginUiState.Loading
                    }

                    is Response.Error -> {
                        _loginUiState.value =
                            LoginUiState.Error(res.exception.error)
                    }

                    is Response.Success -> {
                        val authResult = res.data
                        userLocalRepository.saveUser(
                            authResult.user.toEntity()
                        )

                        _loginUiState.value =
                            LoginUiState.Success(authResult.user, authResult.hasSkills, authResult.isProfileComplete)
                    }
                }
            }
        }
    }

    private fun signup(userData: UserData) {
        viewModelScope.launch {
            authRepository.signup(userData).collect { res ->
                when (res) {

                    is Response.Loading -> {
                        _signupUiState.value = SignupUiState.Loading
                    }

                    is Response.Error -> {
                        _signupUiState.value =
                            SignupUiState.Error(res.exception.error)
                    }

                    is Response.Success -> {
                        val authResult = res.data
                        userLocalRepository.saveUser(
                            authResult.user.toEntity()
                        )

                        _signupUiState.value =
                            SignupUiState.Success(authResult.user, authResult.hasSkills, authResult.isProfileComplete)
                    }
                }
            }
        }
    }

}
