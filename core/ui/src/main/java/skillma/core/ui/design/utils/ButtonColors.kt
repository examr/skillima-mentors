package skillma.core.ui.design.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import skillma.core.ui.theme.Green500
import skillma.core.ui.theme.NeutralBlack11
import skillma.core.ui.theme.NeutralBlack12
import skillma.core.ui.theme.NeutralBlack13
import skillma.core.ui.theme.NeutralWhite10
import skillma.core.ui.theme.NeutralWhite4
import skillma.core.ui.theme.Violet500

sealed class ButtonColor(
    val containerColor: Color,
    val contentColor: Color,
    val disableContainerColor: Color,
    val disableContentColor: Color,
) {

    data object Primary : ButtonColor(
        containerColor = Violet500,
        contentColor = NeutralWhite4,
        disableContainerColor =  Violet500.copy(alpha = 0.4f),
        disableContentColor = NeutralWhite4.copy(0.5f),
    )

    data object Secondary : ButtonColor(
        containerColor = NeutralBlack12,
        contentColor = NeutralWhite4,
        disableContainerColor = NeutralWhite10,
        disableContentColor = NeutralBlack13,
    )

    data object Tertiary : ButtonColor(
        containerColor = NeutralBlack11,
        contentColor = NeutralWhite4,
        disableContainerColor = NeutralWhite10,
        disableContentColor = NeutralBlack13,
    )


}