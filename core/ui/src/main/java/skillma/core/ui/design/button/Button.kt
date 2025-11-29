package skillma.core.ui.design.button

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import skillma.core.ui.design.utils.ButtonColor
import skillma.core.ui.design.utils.ButtonState
import skillma.core.ui.theme.NeutralBlack12

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    state : ButtonState = ButtonState.Idle,
    colors : ButtonColor ,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(2),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disableContainerColor,
            disabledContentColor = colors.disableContentColor,
        ),
        content = {
            if (state is ButtonState.Idle || state is ButtonState.Success) {
                content()
            } else {
                LoadingIndicator(
                    color = NeutralBlack12,
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    )
}


