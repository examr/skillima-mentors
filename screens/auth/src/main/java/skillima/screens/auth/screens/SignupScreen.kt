package skillima.screens.auth.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
import skillma.core.ui.design.logo.SkillimaLogo
import skillma.core.ui.design.utils.ButtonColor
import skillma.core.ui.design.utils.ButtonState
import skillma.core.ui.design.utils.TextFieldColors
import skillma.core.ui.theme.NeutralBlack9
import skillima.core.ui.R as CommonRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    userInput: UserInput,
    signupUiState: SignupUiState,
    onEvent: (AuthEvents) -> Unit,
    navigateToLogin: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    var signupButtonState by remember { mutableStateOf<ButtonState>(ButtonState.Idle) }
    val googleButtonState by remember { mutableStateOf(ButtonState.Idle) }
    val context = LocalContext.current
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
                snackBarHostState.showSnackbar("Success")
                signupButtonState = ButtonState.Success

            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    SkillimaLogo(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(70.dp)
                            .rotate(90f),
                        gridSize = 1,
                        cubeCount = 4
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Sign up",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Welcome to skillima, hope you find it easy",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                SkillimaTextField(
                    color = TextFieldColors.Primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    value = userInput.name,
                    hintValue = "Name",
                    onValueChange = {
                        onEvent(AuthEvents.OnNameChange(it))
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_email),
                            contentDescription = null,
                            tint = NeutralBlack9
                        )
                    },
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
                    color = TextFieldColors.Primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    value = userInput.email,
                    hintValue = "Email Address",
                    onValueChange = {
                        onEvent(AuthEvents.OnEmailChange(it))
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_email),
                            contentDescription = null,
                            tint = NeutralBlack9
                        )
                    },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    color = TextFieldColors.Primary,
                    value = userInput.password,
                    hintValue = "Password",
                    onValueChange = {
                        onEvent(AuthEvents.OnPasswordChange(it))
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_lock),
                            contentDescription = null,
                            tint = NeutralBlack9
                        )
                    },
                    supportingText = {
                        if (userInput.passwordInteracted && !userInput.isPasswordValid) {
                            val error =
                                userInput.passwordValidation.toSingleLineError()

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

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                SkillimaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = userInput.isSignupValid(),
                    state = signupButtonState,
                    colors = ButtonColor.Primary,
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
                    }
                ) {
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.background
                    )
                }

                SkillimaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    state = googleButtonState,
                    colors = ButtonColor.Secondary,
                    onClick = {}
                ) {
                    Text(
                        text = "Continue with Google",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val annotatedString = buildAnnotatedString {
                append("Already have an account? ")
                withStyle(
                    style = SpanStyle(color = MaterialTheme.colorScheme.primary),
                ) {
                    append("Login")
                }
            }

            Text(
                text = annotatedString,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        navigateToLogin()
                    },

                )
        }
    }
}
