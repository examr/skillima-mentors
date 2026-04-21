package skillma.core.ui.design.logo

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
import kotlin.math.cos
import kotlin.math.sin

/**
 * A composable that renders a grid of stacked, animated isometric cubes.
 * The cubes animate in a "wave" sequence, with each cube performing a full 360-degree rotation
 * along its vertical axis.
 *
 * The appearance is based on a specific isometric projection, creating a distinct visual style.
 *
 * @param modifier The modifier to be applied to the canvas.
 * @param gridSize The number of rows and columns in the grid. A value of 1 creates a single column.
 * @param cubeCount The number of cubes stacked in each grid location.
 * @param cubeSize The base size of a cube, which influences its width and height on screen.
 * @param animationDurationMillis The total time in milliseconds for the animation wave to complete
 * a full cycle through all the cubes.
 * @param topColor The color for the top face of the cubes.
 * @param leftColor The color for the left-facing side of the cubes (when visible).
 * @param rightColor The color for the right-facing side of the cubes (when visible).
 * @param bottomColor The color for the bottom face of the cubes (rarely visible).
 * @param backLeftColor The color for the back-left-facing side (used during rotation).
 * @param backRightColor The color for the back-right-facing side (used during rotation).
 */
@Composable
fun AnimatedIsometricCubes(
    modifier: Modifier = Modifier,
    gridSize: Int = 1, // Default to 1 column like your preview
    cubeCount: Int = 4, // Default to 4 stack like your preview
    cubeSize: Float = 400f, // Your requested size
    animationDurationMillis: Int = 2000,
    // Visible Faces
    topColor: Color = Violet500,
    leftColor: Color = Violet600,
    rightColor: Color = Violet700,
    bottomColor: Color = Violet800,
    backLeftColor: Color = Violet400,
    backRightColor: Color = Violet900
) {
    // Animation State
    val transition = rememberInfiniteTransition(label = "CubeSpinner")
    val totalItems = gridSize * gridSize * cubeCount

    // This value goes from 0 to (totalItems + padding) to create a wave effect
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = totalItems + 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2

        // --- 1. EXACT RATIOS FROM YOUR CODE ---
        val spacingRatio = 0.8051f
        val topHeightRatio = 0.2888f
        val sideHeightRatio = 0.5775f

        // --- 2. CALCULATED DIMENSIONS ---
        val topHeight = cubeSize * topHeightRatio
        val fullCubeHeight = (topHeight * 2) + (cubeSize * sideHeightRatio)
        val cubeSpacing = cubeSize * spacingRatio

        // --- 3. CENTERING LOGIC (Identical to your code) ---
        val singleStackHeight = (cubeSpacing * (cubeCount - 1)) + fullCubeHeight
        val gridVerticalOffset = (gridSize - 1) * 2 * topHeight
        val totalStructureHeight = singleStackHeight + gridVerticalOffset
        val startY = ((height - totalStructureHeight) / 2f) + topHeight

        var indexCounter = 0

        // Drawing Loop
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                // Grid offsets
                val gridOffsetX = (row - col) * (cubeSize / 2f)
                val gridOffsetY = (row + col) * topHeight

                // Draw Stack (Bottom to Top for correct overlap)
                for (i in (cubeCount - 1) downTo 0) {
                    val cubeStackY = (i * cubeSpacing)

                    // --- 4. ANIMATION LOGIC ---
                    // Calculate if this specific cube should be rotating right now
                    val globalProgress = phase - indexCounter

                    // coerceIn(0, 1) means: 0 = not started, 1 = finished rotation
                    // The divisor (0.6f) controls how "tight" the wave is.
                    val rotationProgress = (globalProgress / 0.6f).coerceIn(0f, 1f)

                    // PI/4 (45 degrees) is the standard isometric angle.
                    // We add 2*PI (360) to spin it fully around.
                    val angleRad = (Math.PI / 4) + (rotationProgress * Math.PI * 2)

                    drawProjectedCube(
                        centerX = centerX + gridOffsetX,
                        centerY = startY + gridOffsetY + cubeStackY,
                        cubeSize = cubeSize,
                        angleRad = angleRad.toFloat(),
                        topHeightRatio = topHeightRatio,
                        sideHeightRatio = sideHeightRatio,
                        topColor = topColor,
                        leftColor = leftColor,
                        rightColor = rightColor,
                        bottomColor = bottomColor,
                        backLeftColor = backLeftColor,
                        backRightColor = backRightColor
                    )

                    indexCounter++
                }
            }
        }
    }
}

/**
 * Draws a single 3D cube in an isometric-like projection onto the canvas, rotated around its
 * vertical (Y) axis.
 *
 * This function calculates the 2D screen positions of a 3D cube's 8 vertices based on a given
 * rotation angle. It then constructs the cube's faces, sorts them by depth (Z-ordering) to
 * ensure correct overlapping, and draws them. The color of each face is determined dynamically
 * based on its orientation relative to the viewer, ensuring that, for example, the face most
 * oriented to the left always receives the `leftColor`.
 *
 * @param centerX The X-coordinate for the center of the cube on the canvas.
 * @param centerY The Y-coordinate for the center of the cube on the canvas.
 * @param cubeSize The fundamental size of the cube, used as a basis for all dimensional calculations.
 * @param angleRad The rotation angle of the cube around its vertical axis, in radians.
 * @param topHeightRatio The ratio of `cubeSize` used to determine the projected height of the top/bottom faces.
 * @param sideHeightRatio The ratio of `cubeSize` used to determine the projected height of the vertical side faces.
 * @param topColor The color for the top face of the cube.
 * @param leftColor The color for the side face most oriented towards the left.
 * @param rightColor The color for the side face most oriented towards the right.
 * @param bottomColor The color for the bottom face of the cube.
 * @param backLeftColor The color for the hidden side face oriented towards the back-left.
 * @param backRightColor The color for the hidden side face oriented towards the back-right.
 */
fun DrawScope.drawProjectedCube(
    centerX: Float,
    centerY: Float,
    cubeSize: Float,
    angleRad: Float,
    topHeightRatio: Float,
    sideHeightRatio: Float,
    topColor: Color,
    leftColor: Color,
    rightColor: Color,
    bottomColor: Color,
    backLeftColor: Color,
    backRightColor: Color
) {
    val halfWidth = cubeSize / 2f
    val topHeight = cubeSize * topHeightRatio
    val sideHeight = cubeSize * sideHeightRatio

    // --- GEOMETRY GENERATION ---
    // We generate a square on the X/Z plane, rotate it,
    // and then "squash" it by your specific ratios to match the logo look.

    // Scale factor to normalize the rotation math to your specific width
    // At 45 degrees (PI/4), cos(a) = 0.707. We want that to equal 1.0 unit of 'halfWidth'.
    val scale = 1.41421356f // 1 / sin(45)

    // Define the 4 corners of the Top Face (Flat square rotating)
    // 0: Back, 1: Right, 2: Front, 3: Left (Relative to rotation start)
    val rawVertices = listOf(
        Pair(-1f, -1f), Pair(1f, -1f), Pair(1f, 1f), Pair(-1f, 1f)
    )

    // Project the 8 corners (4 Top, 4 Bottom)
    val projectedPoints = (rawVertices + rawVertices).mapIndexed { index, (x, z) ->
        // 1. Rotate around Y axis
        val rx = x * cos(angleRad) - z * sin(angleRad)
        val rz = x * sin(angleRad) + z * cos(angleRad)

        // 2. Project to Screen Coordinates using YOUR Aspect Ratios
        // We map the rotated X to Width, and rotated Z to Height (foreshortening)
        val screenX = rx * (halfWidth * scale * 0.5f)
        val screenY = rz * (topHeight * scale * 0.5f)

        // 3. Shift down for bottom vertices (Indices 4,5,6,7)
        val isBottom = index >= 4
        val yOffset = if (isBottom) sideHeight else 0f

        Offset(centerX + screenX.toFloat(), centerY + screenY.toFloat() + yOffset)
    }

    // Define Faces by Vertex Indices
    data class Face(val indices: List<Int>, val initialNormal: Pair<Float, Float>)

    val faces = listOf(
        Face(listOf(0, 1, 2, 3), Pair(0f, 0f)),    // Top
        Face(listOf(4, 7, 6, 5), Pair(0f, 0f)),    // Bottom
        Face(listOf(2, 1, 5, 6), Pair(1f, 0f)),    // Right (initially)
        Face(listOf(3, 2, 6, 7), Pair(0f, 1f)),    // Front (initially)
        Face(listOf(0, 3, 7, 4), Pair(-1f, 0f)),   // Left (initially)
        Face(listOf(1, 0, 4, 5), Pair(0f, -1f))    // Back (initially)
    )

    // Sort faces by depth (Z-order) so closer faces draw on top
    // We calculate depth based on the rotated Z value
    val sortedFaces = faces.map { face ->
        val avgZ = face.indices.map { idx ->
            val (x, z) = rawVertices[idx % 4]
            x * sin(angleRad) + z * cos(angleRad)
        }.average()
        Pair(face, avgZ)
    }.sortedBy { it.second }

    // Draw Faces
    sortedFaces.forEach { (face, _) ->
        val path = Path().apply {
            moveTo(projectedPoints[face.indices[0]].x, projectedPoints[face.indices[0]].y)
            face.indices.drop(1).forEach { lineTo(projectedPoints[it].x, projectedPoints[it].y) }
            close()
        }

        // --- COLOR LOGIC ---
        // We determine color based on the Rotated Normal Vector.
        // This ensures that a face pointing "Left" is always LeftColor, even if it used to be the Front face.
        val (nx, nz) = face.initialNormal
        val rotatedNx = nx * cos(angleRad) - nz * sin(angleRad)
        val rotatedNz = nx * sin(angleRad) + nz * cos(angleRad)

        val color = when (face) {
            faces[0] -> topColor // Top
            faces[1] -> bottomColor // Bottom
            else -> {
                // Side Faces:
                // If RotatedNz > 0, it is facing the camera.
                // We check RotatedNx to decide if it's Left or Right.
                if (rotatedNz > 0) {
                    if (rotatedNx < 0) leftColor else rightColor
                } else {
                    // Back facing (hidden) sides
                    if (rotatedNx < 0) backLeftColor else backRightColor
                }
            }
        }

        drawPath(path, color)
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedLogoPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            AnimatedIsometricCubes(
                gridSize = 1,
                cubeCount = 4,
                cubeSize = 400f,
                animationDurationMillis = 10000
            )
        }
    }
}