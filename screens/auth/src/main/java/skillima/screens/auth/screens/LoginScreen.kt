package skillima.screens.auth.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import skillima.mentors.module.Role
import skillima.mentors.module.UserData
import skillima.screens.auth.state.AuthEvents
import skillima.screens.auth.state.LoginUiState
import skillima.screens.auth.state.UserInput
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
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginUiState: LoginUiState,
    userInput: UserInput,
    onEvent: (authEvents: AuthEvents) -> Unit,
    navigateToSignup: () -> Unit,
    onSuccess: ()-> Unit
) {

    var loginButtonState by remember { mutableStateOf<ButtonState>(ButtonState.Idle) }
    var googlebButtonState by remember { mutableStateOf<ButtonState>(ButtonState.Idle) }
    val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val context = LocalContext.current

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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { },
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            )
            {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Access your Previous Skills Data Anywhere",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            )
            {
                SkillimaTextField(
                    color = TextFieldColors.Primary,
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = userInput.email,
                    hintValue = "Email Address",
                    onValueChange = { email ->
                        onEvent(
                            AuthEvents.OnEmailChange(email)
                        )
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_email),
                            contentDescription = "",
                            tint = NeutralBlack9
                        )
                    }

                )

                SkillimaPasswordTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = TextFieldColors.Primary,
                    value = userInput.password,
                    hintValue = "Password",
                    onValueChange = { password ->
                        onEvent(AuthEvents.OnPasswordChange(password))
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_lock),
                            contentDescription = "",
                            tint = NeutralBlack9
                        )
                    }

                )
                Text(
                    text = "Forget Password ?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.align(Alignment.End)

                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            )
            {
                SkillimaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = userInput.isLoginValid(),
                    state = loginButtonState,
                    colors = ButtonColor.Primary,
                    onClick = {
                        val userData = UserData(
                            email = userInput.email,
                            password = userInput.password,

                            )
                        onEvent(AuthEvents.Login(userData))
                    }
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.background
                    )
                }

                SkillimaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    state = googlebButtonState,
                    colors = ButtonColor.Secondary,
                    onClick = {

                    }
                ) {
                    Text(
                        text = "Continue with Google",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

            }

            val annotatedString = buildAnnotatedString {
                append("don’t have the accounts yet ? ")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("Create your Account ")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
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
                    },
            )

        }


    }

}