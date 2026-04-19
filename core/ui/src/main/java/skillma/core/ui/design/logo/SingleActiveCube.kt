package skillma.core.ui.design.logo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SingleActiveCube(
    modifier: Modifier = Modifier,
    activeCubeIndex: Int = 0,
    gridSize: Int = 1,
    cubeCount: Int = 4,
    cubeSize: Float = 400f,
    animationDurationMillis: Int = 2000,
    infiniteRotation: Boolean = false, // <--- NEW PARAMETER
    triggerRotation: Boolean = true, // <--- Trigger for single rotation
    onRotationComplete: () -> Unit = {}, // <--- Callback when single rotation completes
    // Colors
    topColor: Color = Color(0xFF00FF48),
    leftColor: Color = Color(0xFF00E842),
    rightColor: Color = Color(0xFF00B533),
    bottomColor: Color = Color(0xFF007A22),
    backLeftColor: Color = Color(0xFF00C036),
    backRightColor: Color = Color(0xFF00942A),
) {
    // For infinite rotation
    val infiniteTransition = rememberInfiniteTransition(label = "CubeSpinner")
    val infiniteRotationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "InfiniteRotation"
    )

    // For single rotation
    val singleRotationProgress = remember { Animatable(0f) }

    // Trigger single rotation when triggerRotation becomes true
    LaunchedEffect(triggerRotation) {
        if (!infiniteRotation && triggerRotation) {
            singleRotationProgress.snapTo(0f)
            singleRotationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = animationDurationMillis,
                    easing = LinearEasing
                )
            )
            onRotationComplete()
        }
    }

    // Choose which rotation progress to use
    val rotationProgress = if (infiniteRotation) {
        infiniteRotationProgress
    } else {
        singleRotationProgress.value
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2

        // Dimensions
        val spacingRatio = 0.8051f
        val topHeightRatio = 0.2888f
        val sideHeightRatio = 0.5775f
        val topHeight = cubeSize * topHeightRatio
        val fullCubeHeight = (topHeight * 2) + (cubeSize * sideHeightRatio)
        val cubeSpacing = cubeSize * spacingRatio

        // Centering
        val singleStackHeight = (cubeSpacing * (cubeCount - 1)) + fullCubeHeight
        val gridVerticalOffset = (gridSize - 1) * 2 * topHeight
        val totalStructureHeight = singleStackHeight + gridVerticalOffset
        val startY = ((height - totalStructureHeight) / 2f) + topHeight

        var indexCounter = 0

        // Loops
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val gridOffsetX = (row - col) * (cubeSize / 2f)
                val gridOffsetY = (row + col) * topHeight

                // Draw Stack (Top to Bottom visual order logic)
                for (i in (cubeCount - 1) downTo 0) {
                    val cubeStackY = (i * cubeSpacing)

                    val isTarget = indexCounter == activeCubeIndex

                    val angleRad = if (isTarget) {
                        (Math.PI / 4) + (rotationProgress * Math.PI * 2)
                    } else {
                        Math.PI / 4
                    }

                    drawProjectedCubes(
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

// --- SHARED DRAWING LOGIC ---
fun DrawScope.drawProjectedCubes(
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
    backRightColor: Color,
) {
    val halfWidth = cubeSize / 2f
    val topHeight = cubeSize * topHeightRatio
    val sideHeight = cubeSize * sideHeightRatio
    val scale = 1.41421356f

    val rawVertices = listOf(
        Pair(-1f, -1f), Pair(1f, -1f), Pair(1f, 1f), Pair(-1f, 1f)
    )

    val projectedPoints = (rawVertices + rawVertices).mapIndexed { index, (x, z) ->
        val rx = x * cos(angleRad) - z * sin(angleRad)
        val rz = x * sin(angleRad) + z * cos(angleRad)
        val screenX = rx * (halfWidth * scale * 0.5f)
        val screenY = rz * (topHeight * scale * 0.5f)
        val isBottom = index >= 4
        val yOffset = if (isBottom) sideHeight else 0f
        Offset(centerX + screenX.toFloat(), centerY + screenY.toFloat() + yOffset)
    }

    data class Face(val indices: List<Int>, val initialNormal: Pair<Float, Float>)

    val faces = listOf(
        Face(listOf(0, 1, 2, 3), Pair(0f, 0f)),
        Face(listOf(4, 7, 6, 5), Pair(0f, 0f)),
        Face(listOf(2, 1, 5, 6), Pair(1f, 0f)),
        Face(listOf(3, 2, 6, 7), Pair(0f, 1f)),
        Face(listOf(0, 3, 7, 4), Pair(-1f, 0f)),
        Face(listOf(1, 0, 4, 5), Pair(0f, -1f))
    )

    // Assign fixed colors to each face based on their index
    val faceColors = listOf(
        topColor,         // Face 0: Top
        bottomColor,      // Face 1: Bottom
        rightColor,       // Face 2: Right (when angleRad = PI/4)
        backRightColor,   // Face 3: Back Right
        backLeftColor,    // Face 4: Back Left
        leftColor         // Face 5: Left (when angleRad = PI/4)
    )

    val sortedFaces = faces.mapIndexed { index, face ->
        // Calculate average Z depth for this face after rotation
        val avgZ = face.indices.map { idx ->
            val (x, z) = rawVertices[idx % 4]
            x * sin(angleRad) + z * cos(angleRad)
        }.average()
        Triple(face, avgZ, faceColors[index])
    }.sortedBy { it.second }

    sortedFaces.forEach { (face, _, color) ->
        val path = Path().apply {
            moveTo(projectedPoints[face.indices[0]].x, projectedPoints[face.indices[0]].y)
            face.indices.drop(1).forEach { lineTo(projectedPoints[it].x, projectedPoints[it].y) }
            close()
        }

        // Calculate if face is visible (facing towards camera)
        val (nx, nz) = face.initialNormal
        val rotatedNx = nx * cos(angleRad) - nz * sin(angleRad)
        val rotatedNz = nx * sin(angleRad) + nz * cos(angleRad)

        // Only draw faces that are facing towards the camera
        // For isometric view, we need to consider both normal components
        val isFacingCamera = when {
            face == faces[0] -> true  // Top always visible from above
            face == faces[1] -> false // Bottom never visible from above
            else -> rotatedNz > 0.1 || (rotatedNz > -0.1 && rotatedNx.toDouble() > 0.1)
        }

        if (isFacingCamera || face == faces[0]) {
            drawPath(path, color)
        }
    }
}

// --- PREVIEW WITH CONTROLS ---
@Preview(showBackground = true)
@Composable
fun SingleActiveCubePreview() {
    var activeIndex by remember { mutableStateOf(0f) }
    var infiniteRotation by remember { mutableStateOf(true) }
    var triggerRotation by remember { mutableStateOf(false) }
    var rotationCount by remember { mutableStateOf(0) }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                SingleActiveCube(
                    modifier = Modifier.fillMaxSize(),
                    activeCubeIndex = activeIndex.toInt(),
                    gridSize = 1,
                    cubeCount = 4,
                    cubeSize = 400f,
                    infiniteRotation = infiniteRotation,
                    triggerRotation = triggerRotation,
                    onRotationComplete = {
                        rotationCount++
                        triggerRotation = false
                    }
                )
            }

            // Controls
            Column(Modifier.padding(16.dp)) {
                Text("Active Cube Index: ${activeIndex.toInt()}")
                Slider(
                    value = activeIndex,
                    onValueChange = { activeIndex = it },
                    valueRange = 0f..3f,
                    steps = 2
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = infiniteRotation,
                        onCheckedChange = { infiniteRotation = it }
                    )
                    Text("Infinite Rotation")
                }

                if (!infiniteRotation) {
                    Button(
                        onClick = { triggerRotation = true },
                        enabled = !triggerRotation
                    ) {
                        Text("Rotate Once")
                    }
                    Text("Rotations completed: $rotationCount")
                }
            }
        }
    }
}

