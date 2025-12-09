package skillma.core.ui.design.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import skillima.core.ui.R

import skillma.core.ui.design.utils.TextFieldColors
import skillma.core.ui.design.utils.TextFieldState
import skillma.core.ui.theme.SkillimaMentorsTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SkillimaTextField(
    modifier: Modifier = Modifier,
    value: String,
    hintValue: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    state: TextFieldState = TextFieldState.Idle,
    color: TextFieldColors = TextFieldColors.Primary,
    textStyle: TextStyle = LocalTextStyle.current,
    navigationIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = RoundedCornerShape(2),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,   // 👈 new
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        placeholder = {
            Text(hintValue, style = textStyle, color = color.hintColor)
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = color.contentColor,
            unfocusedTextColor = color.contentColor,
            focusedContainerColor = color.containerColor,
            unfocusedContainerColor = color.containerColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedPlaceholderColor = color.hintColor,
            unfocusedPlaceholderColor = color.hintColor,
        ),
        trailingIcon = {
            when {
                state == TextFieldState.Loading -> {
                    CircularWavyProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        trackColor = color.containerColor,
                    )
                }
                trailingIcon != null -> trailingIcon()
            }
        },
        isError = state == TextFieldState.Error,
        textStyle = textStyle,
        leadingIcon = navigationIcon,
        singleLine = true,
        visualTransformation = visualTransformation,
        shape = shape,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        supportingText = supportingText
    )
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SkillimaPasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    hintValue: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    state: TextFieldState = TextFieldState.Idle,
    color: TextFieldColors = TextFieldColors.Primary,
    textStyle: TextStyle = LocalTextStyle.current,
    navigationIcon: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(2),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    supportingText: @Composable (() -> Unit)? = null
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    SkillimaTextField(
        modifier = modifier,
        value = value,
        hintValue = hintValue,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        state = state,
        color = color,
        textStyle = textStyle,
        navigationIcon = navigationIcon,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        shape = shape,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        supportingText = supportingText,
        trailingIcon = {
            val icon = if (passwordVisible) {
                    R.drawable.ic_eye_open
            } else {
                R.drawable.ic_eye_close            }
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = painterResource(icon), contentDescription = description,tint = color.hintColor)
            }
        }
    )
}


@Preview
@Composable
fun SkillimaTextFieldPreview() {
    SkillimaMentorsTheme {
        var text by remember {
            mutableStateOf("")
        }

        

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SkillimaTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                hintValue = "Enter your name",
                state = TextFieldState.Loading
            )

            SkillimaTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                hintValue = "Enter your Password",
                state = TextFieldState.Loading
            )
        }

    }
}