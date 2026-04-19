package skillima.screens.onboarding

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import skillma.core.ui.design.button.Button
import skillma.core.ui.design.logo.AnimatedIsometricCubes
import skillma.core.ui.design.logo.SingleActiveCube
import skillma.core.ui.design.utils.ButtonColor
import skillma.core.ui.theme.NeutralBlack12
import skillma.core.ui.theme.NeutralWhite9

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OnBoardingFinalScreen(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    index: Int,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
) {
    with(sharedTransitionScope) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(
                    40.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        30.dp,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState("logo_$index"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    ) {
                        AnimatedIsometricCubes(
                            gridSize = 1,
                            cubeCount = 4,
                            cubeSize = 70f,
                            animationDurationMillis = 10000
                        )
                    }

                    Text(
                        text = "Skillima",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 32.sp
                    )
                }

                Text(
                    text = "Companies Requires Projects, Not Theories",
                    fontSize = 14.sp,
                    color = NeutralWhite9
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onSignupClick,
                    colors = ButtonColor.Secondary,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Sign Up")
                }

                Button(
                    onClick = onLoginClick,
                    colors = ButtonColor.Primary,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Login")
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OnBoardingPageContent(
    modifier: Modifier = Modifier,
    index: Int = 0,
    title: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    with(sharedTransitionScope) {
        Column(
            modifier = modifier
                .padding(start = 30.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(40.dp, alignment = Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp, alignment = Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("logo_$index"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .padding(5.dp)
                ) {
                    SingleActiveCube(
                        gridSize = 1,
                        cubeCount = 4,
                        cubeSize = 100f,
                        activeCubeIndex = index
                    )
                }

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 40.sp,
                    fontSize = 28.sp,
                    maxLines = 3,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(topStartPercent = 4, bottomStartPercent = 4))
                    .background(NeutralBlack12),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Later UIs",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
