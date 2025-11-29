package skillma.core.ui.design.utils

import androidx.compose.ui.graphics.Color
import skillma.core.ui.theme.NeutralBlack12
import skillma.core.ui.theme.NeutralBlack13
import skillma.core.ui.theme.NeutralBlack9
import skillma.core.ui.theme.NeutralWhite10
import skillma.core.ui.theme.NeutralWhite4

sealed class TextFieldColors(
    val containerColor: Color,
    val contentColor: Color,
    val hintColor : Color,
    val disableContainerColor: Color,
    val disableContentColor: Color,
) {
    data object Primary : TextFieldColors(
        containerColor = NeutralBlack12,
        contentColor = NeutralWhite4,
        disableContainerColor = NeutralBlack13,
        disableContentColor = NeutralWhite10,
        hintColor = NeutralBlack9,
    )
}