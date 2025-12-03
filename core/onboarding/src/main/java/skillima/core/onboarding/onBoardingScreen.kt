package skillima.core.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import skillima.core.ui.R as CommonRes
import skillma.core.ui.theme.NeutralBlack12

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    onFinished: () -> Unit = {}
) {

    // text for each page
    val pages = listOf(
        "Build Projects\nThat Actually\nMatters",
        "Get Mentorship\non Projects you\nbuild",
        "Community That\nShape your\nInterests"
    )

    val pagerState = rememberPagerState(initialPage = 0) { pages.size }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Black  // background like your design
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // --- PAGER CONTENT ---
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    // Top: logo + text
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(CommonRes.drawable.skillima_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .width(60.dp)
                                .height(110.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = pages[page],
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 3.sp
                        )
                    }

                    // Middle card placeholder ("Later UIs")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .fillMaxHeight(0.7f)
                                .background(
                                    Color(0xFF111111),
                                    shape = RoundedCornerShape(
                                        topStart = 30.dp,
                                        bottomStart = 30.dp
                                    )
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .fillMaxHeight(0.65f)
                                    .background(
                                        MaterialTheme.colorScheme.background,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .align(Alignment.Center),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Later UIs",
                                    color = Color.White,
                                )
                            }
                        }
                    }

                    // bottom space is handled by bottom nav row (overlay)
                    Spacer(modifier = Modifier.height(64.dp))
                }
            }

            // --- BOTTOM NAV BUTTONS (prev / next) ---
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button (hidden on first page)
                if (pagerState.currentPage > 0) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = NeutralBlack12,   // button background
                            contentColor = Color.White            // icon color
                        ),
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_down_left),
                            contentDescription = "Previous",
                            tint = Color.White,
                            modifier = Modifier
                                .rotate(90f) // rotated  UP
                                .size(24.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp)) // keep layout balanced
                }

                // Next / Done button
                IconButton(
                    onClick = {
                        scope.launch {
                            val lastPage = pages.lastIndex
                            if (pagerState.currentPage < lastPage) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            } else {
                                onFinished() // go to main app
                            }
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = NeutralBlack12,   // button background
                        contentColor = Color.White            // icon color
                    ),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_down_right),
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier
                            .rotate(-90f)   // rotate UP
                            .size(24.dp)
                    )

                }
            }
        }
    }
}
