package skillima.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import skillma.core.ui.design.button.SkillimaButton
import skillma.core.ui.design.logo.SkillimaLogo
import skillma.core.ui.design.utils.ButtonColor
import skillima.core.ui.R as CommonRes

import skillma.core.ui.design.utils.ButtonState

@OptIn(ExperimentalTextApi::class)
@Composable
fun WelcomeScreen(
    navigateToLogin: () -> Unit,
    navigateToSignup: () -> Unit
) {

    val tagline = "Companies Requires Projects, Not\nTheories"
    val textMeasurer = rememberTextMeasurer()

    // Measure tagline exact width
    val textLayout = textMeasurer.measure(
        text = tagline,
        style = MaterialTheme.typography.bodyMedium
    )

    val taglineWidth = textLayout.size.width

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {


            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {


                Row(
                    modifier = Modifier.width(with(LocalDensity.current) { taglineWidth.toDp() }),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    SkillimaLogo(
                        modifier = Modifier
                            .width(50.dp)
                            .height(100.dp)
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    Text(
                        text = "Skillima",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }


                Text(
                    text = tagline,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(with(LocalDensity.current) { taglineWidth.toDp() })
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SkillimaButton(
                    state = ButtonState.Idle,
                    onClick = {},
                    colors = ButtonColor.Secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Continue With Google")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SkillimaButton(
                        state = ButtonState.Idle,
                        onClick = { navigateToSignup() },
                        colors = ButtonColor.Secondary,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(text = "Sign up")
                    }

                    SkillimaButton(
                        state = ButtonState.Idle,
                        onClick = { navigateToLogin() },
                        colors = ButtonColor.Primary,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(text = "Login")
                    }
                }
            }
        }
    }
}

