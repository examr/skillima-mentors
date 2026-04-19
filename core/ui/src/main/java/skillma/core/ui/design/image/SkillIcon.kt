package skillma.core.ui.design.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent

@Composable
fun SkillIcon(
    name: String,
    iconUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    cornerRadius: Dp = 10.dp,
) {
    val backgroundColor = remember(name) { skillBackgroundColor(name) }
    val iconPadding = remember(size) { (size * 0.15f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor),
    ) {
        if (iconUrl != null) {
            SubcomposeAsyncImage(
                model = iconUrl,
                contentDescription = "$name icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(size - iconPadding * 2)
                    .padding(iconPadding),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.size(size * 0.4f),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        strokeWidth = 1.5.dp,
                    )
                },
                error = {
                    SkillInitials(name = name, backgroundColor = backgroundColor)
                },
                success = {
                    SubcomposeAsyncImageContent()
                },
            )
        } else {
            SkillInitials(name = name, backgroundColor = backgroundColor)
        }
    }
}

@Composable
private fun SkillInitials(
    name: String,
    backgroundColor: Color,
) {
    val textColor = Color(0xFFEEEEEE)
    Text(
        text = skillInitials(name),
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        maxLines = 1,
    )
}

private fun skillBackgroundColor(name: String): Color {
    val palette = listOf(
        Color(0xFF0D1B2A), Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460),
        Color(0xFF1B1B2F), Color(0xFF2D132C), Color(0xFF1A0A2E), Color(0xFF0A2E1A),
        Color(0xFF1A2E0A), Color(0xFF2E1A0A), Color(0xFF2E0A0A), Color(0xFF0A2E2E),
        Color(0xFF1C1C1C), Color(0xFF212121), Color(0xFF263238), Color(0xFF1B2631),
    )
    val hash = name.fold(5381L) { acc, c -> acc * 33 + c.code }
    val index = Math.floorMod(hash, palette.size.toLong()).toInt()
    return palette[index]
}

internal fun skillInitials(name: String): String {
    val cleaned = name.trim()
    if (!cleaned.contains(' ') && !cleaned.contains('.')) {
        return cleaned.take(2).uppercase()
    }
    val dotExtPattern = Regex("""^(\w+)\.(js|ts|css|py)$""", RegexOption.IGNORE_CASE)
    dotExtPattern.find(cleaned)?.let { match ->
        return match.groupValues[1].take(2).uppercase()
    }
    val words = cleaned
        .split(' ', '.', '/', '-', '&')
        .filter { it.isNotBlank() }
    return when {
        words.size >= 2 -> "${words[0].first()}${words[1].first()}".uppercase()
        words.size == 1 -> words[0].take(2).uppercase()
        else            -> cleaned.take(2).uppercase()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun SkillIconPreview() {
    val previewSkills = listOf(
        "React.js" to "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/react/react-original.svg",
        "Kotlin" to "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/kotlin/kotlin-original.svg",
        "TypeScript" to null,
        "No URL Skill" to null,
    )
    FlowRow(
        modifier = Modifier.padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        previewSkills.forEach { (name, url) ->
            SkillIcon(name = name, iconUrl = url, size = 56.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SkillIconSmallPreview() {
    val skills = listOf("AWS", "Redis", "MongoDB", "Kotlin", "Swift")
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        skills.forEach { name ->
            SkillIcon(name = name, iconUrl = null, size = 36.dp, cornerRadius = 8.dp)
        }
    }
}
