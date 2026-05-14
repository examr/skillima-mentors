package skillima.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import skillma.core.ui.theme.Blue400
import skillma.core.ui.theme.Green500
import skillma.core.ui.theme.NeutralBlack7
import skillma.core.ui.theme.NeutralBlack13
import skillma.core.ui.theme.Orange400
import skillma.core.ui.theme.Violet500
import skillma.core.ui.theme.displayFontFamily
import kotlin.math.absoluteValue

private val CardBorderColor = Color(0xFF242424)
private val CardBackground = Color(0xFF0A0A0A)

data class AwaitingCardData(
    val title: String,
    val glowColor: Color,
    val icon: ImageVector,
)

private val awaitingCards = listOf(
    AwaitingCardData(
        title = "Reserved For\nVerified Only.",
        glowColor = Violet500,
        icon = Icons.Outlined.Verified,
    ),
    AwaitingCardData(
        title = "Build Your Own\nNetwork.",
        glowColor = Orange400,
        icon = Icons.Outlined.Groups,
    ),
    AwaitingCardData(
        title = "Unlock Real\nOpportunity.",
        glowColor = Green500,
        icon = Icons.Outlined.TrendingUp,
    ),
    AwaitingCardData(
        title = "Earn With\nFlexibility.",
        glowColor = Blue400,
        icon = Icons.Outlined.Savings,
    ),
)

@Composable
fun MentorAwaitingScreen(
    onLogout: () -> Unit,
    logoutState: LogoutState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NeutralBlack13)
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AwaitingHeader(
                onLogout = onLogout,
                logoutState = logoutState,
            )
            AwaitingPager(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun AwaitingHeader(
    onLogout: () -> Unit,
    logoutState: LogoutState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 44.dp, bottom = 28.dp),
    ) {
        Text(
            text = "profile is being\nverified",
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.Black,
            fontSize = 38.sp,
            lineHeight = 42.sp,
            letterSpacing = (-0.8).sp,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "your profile is being verified, will update you once you are verified.",
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            letterSpacing = 0.sp,
            color = NeutralBlack7,
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
            onClick = onLogout,
            enabled = logoutState !is LogoutState.Loading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White.copy(alpha = 0.7f),
                containerColor = Color.Transparent,
                disabledContentColor = Color.White.copy(alpha = 0.3f),
            ),
            border = BorderStroke(0.5.dp, CardBorderColor),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            Text(
                text = if (logoutState is LogoutState.Loading) "Signing out…" else "Log Out",
                fontFamily = displayFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
            )
        }
    }
}

@Composable
private fun AwaitingPager(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState { awaitingCards.size }

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(start = 24.dp, end = 60.dp),
        pageSpacing = 14.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 36.dp),
    ) { page ->
        val rawOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
        val pageOffset = rawOffset.absoluteValue.coerceIn(0f, 1f)
        val scale = lerp(start = 0.93f, stop = 1f, fraction = 1f - pageOffset)

        MentorAwaitingCard(
            card = awaitingCards[page],
            modifier = Modifier
                .fillMaxHeight()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    alpha = lerp(start = 0.72f, stop = 1f, fraction = 1f - pageOffset)
                },
        )
    }
}

@Composable
fun MentorAwaitingCard(
    card: AwaitingCardData,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.50f,
        targetValue = 0.80f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glowAlpha",
    )
    val glowRadius by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 0.88f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glowRadius",
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(CardBackground)
            .border(
                width = 0.5.dp,
                color = CardBorderColor,
                shape = RoundedCornerShape(6.dp),
            ),
    ) {
        AtmosphericGlow(
            color = card.glowColor,
            glowAlpha = glowAlpha,
            glowRadius = glowRadius,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                imageVector = card.icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.35f),
                modifier = Modifier.size(26.dp),
            )

            Column {
                Text(
                    text = card.title,
                    fontFamily = displayFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 36.sp,
                    lineHeight = 40.sp,
                    letterSpacing = (-0.8).sp,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(24.dp))
                LearnMoreButton()
            }
        }
    }
}

@Composable
private fun AtmosphericGlow(
    color: Color,
    glowAlpha: Float,
    glowRadius: Float,
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Primary large outer glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = 0.38f * glowAlpha),
                    color.copy(alpha = 0.18f * glowAlpha),
                    Color.Transparent,
                ),
                center = Offset(size.width / 2f, size.height * 0.90f),
                radius = size.width * glowRadius,
            ),
        )
        // Inner concentrated glow at the very bottom
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = 0.55f * glowAlpha),
                    color.copy(alpha = 0.20f * glowAlpha),
                    Color.Transparent,
                ),
                center = Offset(size.width / 2f, size.height * 0.98f),
                radius = size.width * 0.42f,
            ),
        )
        // Horizon spread
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = 0.12f * glowAlpha),
                    Color.Transparent,
                ),
                center = Offset(size.width / 2f, size.height * 0.75f),
                radius = size.width * 0.95f,
            ),
        )
    }
}

@Composable
private fun LearnMoreButton() {
    OutlinedButton(
        onClick = {},
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.White,
            containerColor = Color.Transparent,
        ),
        border = BorderStroke(0.5.dp, CardBorderColor),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Learn More",
                fontFamily = displayFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                letterSpacing = 0.8.sp,
            )
            Text(
                text = "——→",
                fontFamily = displayFontFamily,
                fontWeight = FontWeight.Thin,
                fontSize = 13.sp,
                letterSpacing = 0.sp,
                color = Color.White.copy(alpha = 0.6f),
            )
        }
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float =
    start + (stop - start) * fraction
