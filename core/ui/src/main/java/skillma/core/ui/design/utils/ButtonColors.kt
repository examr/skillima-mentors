package skillma.core.ui.design.utils

import androidx.compose.ui.graphics.Color
import skillma.core.ui.theme.Green500
import skillma.core.ui.theme.NeutralBlack11
import skillma.core.ui.theme.NeutralBlack12
import skillma.core.ui.theme.NeutralBlack13
import skillma.core.ui.theme.NeutralWhite10
import skillma.core.ui.theme.NeutralWhite4

sealed class ButtonColor(
    val containerColor: Color,
    val contentColor: Color,
    val disableContainerColor: Color,
    val disableContentColor: Color,
) {

    data object Primary : ButtonColor(
        containerColor = Green500,
        contentColor = NeutralBlack13,
        disableContainerColor = NeutralWhite10,
        disableContentColor = NeutralBlack13,
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