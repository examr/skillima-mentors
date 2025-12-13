package skillma.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppDarkColorScheme = darkColorScheme(
    primary = Violet500,
    onPrimary = NeutralBlack13,         // readable on bright green
    primaryContainer = Violet700,
    onPrimaryContainer = NeutralWhite1,

    secondary = Blue300,
    onSecondary = NeutralWhite1,
    secondaryContainer = Blue700,
    onSecondaryContainer = NeutralBlack12,

    tertiary = Green300,
    onTertiary = NeutralWhite1,

    background = NeutralBlack13,        // main app background (very dark)
    onBackground = NeutralWhite1,

    surface = NeutralBlack11,           // cards, sheets, etc.
    onSurface = NeutralWhite1,

    surfaceVariant = NeutralBlack9,
    onSurfaceVariant = NeutralWhite1,

    error = Orange700,
    onError = NeutralWhite1,

    outline = NeutralBlack8,
    inverseOnSurface = NeutralBlack13,
    inverseSurface = NeutralWhite1,
    inversePrimary = Violet300
)

@Composable
fun SkillimaMentorsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppDarkColorScheme,
        typography = AppTypography,
        content = content
    )
}