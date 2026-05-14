package skillima.screens.auth.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import skillima.mentors.module.UserData
import skillima.screens.auth.state.AuthEvents
import skillima.screens.auth.state.SignupUiState
import skillima.screens.auth.state.UserInput
import skillima.screens.auth.utils.toSingleLineError
import skillma.core.ui.design.button.SkillimaButton
import skillma.core.ui.design.input.SkillimaPasswordTextField
import skillma.core.ui.design.input.SkillimaTextField
import skillma.core.ui.design.logo.IsometricCubes
import skillma.core.ui.design.utils.ButtonColor
import skillma.core.ui.design.utils.ButtonState
import skillma.core.ui.design.utils.TextFieldColors
import skillma.core.ui.theme.NeutralBlack9
import skillma.core.ui.theme.NeutralWhite9
import skillima.core.ui.R as CommonRes

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    userInput: UserInput,
    signupUiState: SignupUiState,
    onEvent: (AuthEvents) -> Unit,
    navigateToLogin: () -> Unit,
    onSuccess: (isProfileComplete: Boolean) -> Unit
) {
    val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }
    var signupButtonState by remember { mutableStateOf<ButtonState>(ButtonState.Idle) }
    val context = LocalContext.current
    var isFormDisabled by remember { mutableStateOf(false) }

    LaunchedEffect(signupUiState) {
        when (signupUiState) {
            is SignupUiState.Error -> {
                signupButtonState = ButtonState.Idle
                val msg = context.getString(signupUiState.errorMessage)
                snackBarHostState.showSnackbar(msg)
            }
            SignupUiState.Idle -> {
                signupButtonState = ButtonState.Idle
            }
            SignupUiState.Loading -> {
                signupButtonState = ButtonState.Loading
            }
            is SignupUiState.Success -> {
                onSuccess(signupUiState.isProfileComplete)
                signupButtonState = ButtonState.Success
            }
        }
    }

    LaunchedEffect(signupButtonState) {
        isFormDisabled = signupButtonState == ButtonState.Loading
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IsometricCubes(
                            gridSize = 1,
                            cubeCount = 4,
                            cubeSize = 70f,
                            horizontal = true,
                        )
                    }
                },
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            Text(
                text = "Sign up",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Welcome to Skillima, Hope you find it easy",
                style = MaterialTheme.typography.bodyMedium,
                color = NeutralWhite9,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.fillMaxWidth().padding(1.dp)
            ) {
                SkillimaTextField(
                    value = userInput.name,
                    onValueChange = { onEvent(AuthEvents.OnNameChange(it)) },
                    hintValue = "Name",
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = CommonRes.drawable.ic_user),
                            contentDescription = "Name",
                            tint = NeutralBlack9
                        )
                    },
                    color = TextFieldColors.Primary,
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                    supportingText = {
                        if (userInput.nameInteracted && !userInput.isNameValid) {
                            Text(
                                text = "Name must be at least 5 characters",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                )

                SkillimaTextField(
                    value = userInput.email,
                    onValueChange = { onEvent(AuthEvents.OnEmailChange(it)) },
                    hintValue = "Email",
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = CommonRes.drawable.ic_email),
                            contentDescription = "Email Address",
                            tint = NeutralBlack9
                        )
                    },
                    color = TextFieldColors.Primary,
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                    supportingText = {
                        if (userInput.emailInteracted && !userInput.isEmailValid) {
                            Text(
                                text = "Invalid email address",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                )

                SkillimaPasswordTextField(
                    value = userInput.password,
                    onValueChange = { onEvent(AuthEvents.OnPasswordChange(it)) },
                    hintValue = "Password",
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = CommonRes.drawable.ic_lock),
                            contentDescription = "Password",
                            tint = NeutralBlack9
                        )
                    },
                    color = TextFieldColors.Primary,
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                    supportingText = {
                        if (userInput.passwordInteracted && !userInput.isPasswordValid) {
                            val error = userInput.passwordValidation.toSingleLineError()
                            if (error.isNotEmpty()) {
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                )
            }

            SkillimaButton(
                onClick = {
                    onEvent(
                        AuthEvents.Signup(
                            UserData(
                                email = userInput.email,
                                password = userInput.password,
                                name = userInput.name
                            )
                        )
                    )
                },
                state = signupButtonState,
                colors = ButtonColor.Primary,
                enabled = userInput.isSignupValid(),
                modifier = Modifier.fillMaxWidth().height(65.dp).padding(5.dp)
            ) {
                Text(text = "Create my Account", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.weight(1f))

            val annotatedString = buildAnnotatedString {
                append("already have an account? ")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("Login ")
                }
            }
            Text(
                text = annotatedString,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        navigateToLogin()
                    }
            )
        }
    }
}
