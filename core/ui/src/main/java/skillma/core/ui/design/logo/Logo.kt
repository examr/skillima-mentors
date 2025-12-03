package skillma.core.ui.design.logo


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import skillma.core.ui.theme.Violet400
import skillma.core.ui.theme.Violet500
import skillma.core.ui.theme.Violet600
import skillma.core.ui.theme.Violet700
import skillma.core.ui.theme.Violet800
import skillma.core.ui.theme.Violet900
import kotlin.math.min


enum class LogoOrientation {
    Vertical,
    Horizontal
}

@Composable
fun SkillimaLogo(
    modifier: Modifier = Modifier,
    gridSize: Int = 1,      // you mainly use 1
    cubeCount: Int = 4,     // vertical stack count
    cubeSize: Float = 141f, // max desired size in px

    // VIOLET palette
    topColor: Color = Violet500,
    leftColor: Color = Violet600,
    rightColor: Color = Violet700,
    bottomColor: Color = Violet800,
    backLeftColor: Color = Violet400,
    backRightColor: Color = Violet900
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f

        // Ratios derived from original SVG values
        val spacingRatio = 0.8051f
        val topHeightRatio = 0.2888f
        val sideHeightRatio = 0.5775f

        // Total height of a column in terms of cubeSize:
        // H = cubeSize * (spacingRatio*(cubeCount-1) + (2*topHeightRatio + sideHeightRatio))
        val heightFactor =
            spacingRatio * (cubeCount - 1) + (2 * topHeightRatio + sideHeightRatio)

        // Choose cubeSize so the whole stack fits into the canvas
        val maxByHeight = if (heightFactor > 0f) height / heightFactor else cubeSize
        val maxByWidth = width // cube width is cubeSize
        val cubeSizePx = min(cubeSize, min(maxByHeight, maxByWidth))

        // Calculated dimensions with dynamic size
        val halfWidth = cubeSizePx / 2f
        val cubeSpacing = cubeSizePx * spacingRatio
        val topHeight = cubeSizePx * topHeightRatio
        val sideHeight = cubeSizePx * sideHeightRatio
        val fullCubeHeight = (topHeight * 2f) + sideHeight

        // Centering Calculations
        val singleStackHeight = (cubeSpacing * (cubeCount - 1)) + fullCubeHeight
        val gridVerticalOffset = (gridSize - 1) * 2f * topHeight
        val totalStructureHeight = singleStackHeight + gridVerticalOffset
        val startY = ((height - totalStructureHeight) / 2f) + topHeight

        // Drawing Loop (Back-to-Front painter's algorithm)
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {

                val gridOffsetX = (row - col) * halfWidth
                val gridOffsetY = (row + col) * topHeight

                for (i in (cubeCount - 1) downTo 0) {
                    val cubeStackY = i * cubeSpacing

                    drawIsometricCube(
                        centerX = centerX + gridOffsetX,
                        centerY = startY + gridOffsetY + cubeStackY,
                        cubeSize = cubeSizePx,
                        topColor = topColor,
                        leftColor = leftColor,
                        rightColor = rightColor,
                        bottomColor = bottomColor,
                        backLeftColor = backLeftColor,
                        backRightColor = backRightColor,
                        topHeightRatio = topHeightRatio,
                        sideHeightRatio = sideHeightRatio
                    )
                }
            }
        }
    }
}



fun DrawScope.drawIsometricCube(
    centerX: Float,
    centerY: Float,
    cubeSize: Float,
    topColor: Color,
    leftColor: Color,
    rightColor: Color,
    bottomColor: Color,
    backLeftColor: Color,
    backRightColor: Color,
    topHeightRatio: Float,
    sideHeightRatio: Float
) {
    val halfWidth = cubeSize / 2f
    val topHeight = cubeSize * topHeightRatio
    val sideHeight = cubeSize * sideHeightRatio

    // --- Key Points Relative to Center (Center of Top Face) ---
    // Top Tip: (centerX, centerY - topHeight)
    // Left Tip: (centerX - halfWidth, centerY)
    // Right Tip: (centerX + halfWidth, centerY)
    // Bottom Tip (of diamond): (centerX, centerY + topHeight)
    // "Down" vector is just +sideHeight to Y

    // --- DRAWING HIDDEN FACES FIRST (Background) ---

    // 1. Bottom Face (Identical to Top Face, but shifted down by sideHeight)
    val bottomPath = Path().apply {
        moveTo(centerX, centerY - topHeight + sideHeight)
        lineTo(centerX + halfWidth, centerY + sideHeight)
        lineTo(centerX, centerY + topHeight + sideHeight)
        lineTo(centerX - halfWidth, centerY + sideHeight)
        close()
    }
    drawPath(bottomPath, bottomColor)

    // 2. Back Left Face (Connects Top-Tip and Left-Tip downwards)
    val backLeftPath = Path().apply {
        moveTo(centerX, centerY - topHeight) // Top Tip
        lineTo(centerX - halfWidth, centerY) // Left Tip
        lineTo(centerX - halfWidth, centerY + sideHeight) // Left Tip Down
        lineTo(centerX, centerY - topHeight + sideHeight) // Top Tip Down
        close()
    }
    drawPath(backLeftPath, backLeftColor)

    // 3. Back Right Face (Connects Top-Tip and Right-Tip downwards)
    val backRightPath = Path().apply {
        moveTo(centerX, centerY - topHeight) // Top Tip
        lineTo(centerX + halfWidth, centerY) // Right Tip
        lineTo(centerX + halfWidth, centerY + sideHeight) // Right Tip Down
        lineTo(centerX, centerY - topHeight + sideHeight) // Top Tip Down
        close()
    }
    drawPath(backRightPath, backRightColor)

    // --- DRAWING VISIBLE FACES LAST (Foreground) ---

    // 4. Top Face
    val topPath = Path().apply {
        moveTo(centerX, centerY - topHeight)
        lineTo(centerX + halfWidth, centerY)
        lineTo(centerX, centerY + topHeight)
        lineTo(centerX - halfWidth, centerY)
        close()
    }
    drawPath(topPath, topColor)

    // 5. Front Left Face
    val leftPath = Path().apply {
        moveTo(centerX - halfWidth, centerY)
        lineTo(centerX, centerY + topHeight)
        lineTo(centerX, centerY + topHeight + sideHeight)
        lineTo(centerX - halfWidth, centerY + sideHeight)
        close()
    }
    drawPath(leftPath, leftColor)

    // 6. Front Right Face
    val rightPath = Path().apply {
        moveTo(centerX + halfWidth, centerY)
        lineTo(centerX + halfWidth, centerY + sideHeight)
        lineTo(centerX, centerY + topHeight + sideHeight)
        lineTo(centerX, centerY + topHeight)
        close()
    }
    drawPath(rightPath, rightColor)
}
