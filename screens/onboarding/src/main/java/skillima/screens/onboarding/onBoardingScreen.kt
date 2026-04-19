package skillima.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import skillma.core.ui.theme.NeutralBlack12
import skillma.core.ui.theme.NeutralWhite3

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
    onSignupClick: () -> Unit = {},
) {
    val pages = listOf(
        "Build Projects\nThat Actually\nMatters",
        "Get Mentorship\non Projects you\nbuild",
        "Community That\nShape your\nInterests"
    )

    var showLogin by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(initialPage = 0) { pages.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (showLogin || pagerState.currentPage > 0) {
            showLogin = false
            pagerState.scrollToPage(0)
        }
    }

    SharedTransitionLayout {
        AnimatedContent(targetState = showLogin) { isLogin ->
            if (isLogin) {
                OnBoardingFinalScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    index = pagerState.currentPage,
                    onLoginClick = onLoginClick,
                    onSignupClick = onSignupClick,
                )
            } else {
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .weight(1f)
                    ) { page ->
                        OnBoardingPageContent(
                            title = pages[page],
                            index = page,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        if (pagerState.currentPage > 0) {
                            FilledIconButton(
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                },
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = NeutralBlack12,
                                    contentColor = NeutralWhite3
                                ),
                                modifier = Modifier
                                    .size(64.dp)
                                    .align(Alignment.BottomStart)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_arrow_down_left),
                                    contentDescription = "Previous",
                                )
                            }
                        }

                        FilledIconButton(
                            onClick = {
                                scope.launch {
                                    if (pagerState.canScrollForward) {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    } else {
                                        showLogin = true
                                    }
                                }
                            },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = NeutralBlack12,
                                contentColor = NeutralWhite3
                            ),
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.BottomEnd)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_down_left),
                                contentDescription = "Next",
                                modifier = Modifier.rotate(180f),
                            )
                        }
                    }
                }
            }
        }
    }
}
