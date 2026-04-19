package skillma.core.ui.design.utils

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass

@Suppress("ParamsComparedByRef")
@Composable
fun AdaptiveUI(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    content: @Composable (isLandscape: Boolean) -> Unit
) {
    val isLandscape = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
    content(isLandscape)
}
