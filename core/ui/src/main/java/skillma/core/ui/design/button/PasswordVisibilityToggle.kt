package skillma.core.ui.design.button

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import skillma.core.ui.theme.CommonDrawable

@Composable
fun PasswordVisibilityToggle(
    isVisible: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.White
) {
    // Spring scale punch on every toggle
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.75f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        finishedListener = { pressed = false },
        label = "scale"
    )

    Crossfade(
        targetState = isVisible,
        animationSpec = tween(durationMillis = 200),
        modifier = modifier
            .size(24.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null  // no ripple — scale handles feedback
            ) {
                pressed = true
                onToggle()
            },
        label = "eye_crossfade"
    ) { visible ->
        Image(
            painter = painterResource(
                id = if (visible) CommonDrawable.ic_password_show
                     else CommonDrawable.ic_password_hide
            ),
            contentDescription = if (visible) "Hide password" else "Show password",
            colorFilter = ColorFilter.tint(tint)
        )
    }
}
