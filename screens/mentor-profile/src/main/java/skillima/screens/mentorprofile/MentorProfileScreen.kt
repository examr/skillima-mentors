package skillima.screens.mentorprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import skillma.core.ui.design.button.SkillimaButton
import skillma.core.ui.design.utils.ButtonColor
import skillma.core.ui.design.utils.ButtonState

@Composable
fun MentorProfileScreen(
    uiState: MentorProfileUiState,
    onSave: (bio: String, githubUrl: String, linkedinUrl: String, xUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var bio by remember { mutableStateOf("") }
    var githubUrl by remember { mutableStateOf("") }
    var linkedinUrl by remember { mutableStateOf("") }
    var xUrl by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Complete Your Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Tell the community about yourself before selecting your guilds.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio *") },
            placeholder = { Text("Tell us about your experience and expertise...") },
            minLines = 3,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth(),
            isError = uiState is MentorProfileUiState.Error && bio.isBlank(),
        )

        OutlinedTextField(
            value = githubUrl,
            onValueChange = { githubUrl = it },
            label = { Text("GitHub URL") },
            placeholder = { Text("https://github.com/username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        OutlinedTextField(
            value = linkedinUrl,
            onValueChange = { linkedinUrl = it },
            label = { Text("LinkedIn URL") },
            placeholder = { Text("https://linkedin.com/in/username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        OutlinedTextField(
            value = xUrl,
            onValueChange = { xUrl = it },
            label = { Text("X (Twitter) URL") },
            placeholder = { Text("https://x.com/username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        if (uiState is MentorProfileUiState.Error) {
            Text(
                text = uiState.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        SkillimaButton(
            onClick = { onSave(bio, githubUrl, linkedinUrl, xUrl) },
            colors = ButtonColor.Primary,
            state = if (uiState is MentorProfileUiState.Loading) ButtonState.Loading else ButtonState.Idle,
            enabled = bio.isNotBlank() && uiState !is MentorProfileUiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Continue")
        }
    }
}
