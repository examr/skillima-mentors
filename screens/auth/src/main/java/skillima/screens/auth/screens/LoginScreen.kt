package skillima.screens.auth.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import skillima.mentors.module.UserData
import skillima.screens.auth.state.AuthEvents
import skillima.screens.auth.state.LoginUiState
import skillima.screens.auth.state.UserInput
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
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginUiState: LoginUiState,
    userInput: UserInput,
    onEvent: (authEvents: AuthEvents) -> Unit,
    navigateToSignup: () -> Unit,
    onSuccess: () -> Unit
) {
    var loginButtonState by remember { mutableStateOf<ButtonState>(ButtonState.Idle) }
    val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val context = LocalContext.current
    var isFormDisabled by remember { mutableStateOf(false) }

    LaunchedEffect(loginUiState) {
        when (loginUiState) {
            is LoginUiState.Error -> {
                val message = context.getString(loginUiState.errorMessage)
                snackBarHostState.showSnackbar(message)
                loginButtonState = ButtonState.Idle
            }
            LoginUiState.Idle -> {
                loginButtonState = ButtonState.Idle
            }
            LoginUiState.Loading -> {
                loginButtonState = ButtonState.Loading
            }
            is LoginUiState.Success -> {
                onSuccess()
                loginButtonState = ButtonState.Success
            }
        }
    }

    LaunchedEffect(loginButtonState) {
        isFormDisabled = loginButtonState == ButtonState.Loading
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
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Access your Previous Skills Data Anywhere",
                style = MaterialTheme.typography.bodyMedium,
                color = NeutralWhite9,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                modifier = Modifier.fillMaxWidth().padding(5.dp)
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
                modifier = Modifier.fillMaxWidth().padding(5.dp)
            )

            SkillimaButton(
                onClick = {
                    onEvent(
                        AuthEvents.Login(
                            UserData(
                                email = userInput.email,
                                password = userInput.password,
                            )
                        )
                    )
                },
                state = loginButtonState,
                colors = ButtonColor.Primary,
                enabled = userInput.isLoginValid(),
                modifier = Modifier.fillMaxWidth().height(65.dp).padding(5.dp)
            ) {
                Text(text = "Login", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.weight(1f))

            val annotatedString = buildAnnotatedString {
                append("don't have the accounts yet ? ")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("Create your Account ")
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
                        navigateToSignup()
                    }
            )
        }
    }
}
