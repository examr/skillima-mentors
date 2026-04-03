package skillima.screens.guild.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import skillima.mentors.navigation.Navigator
import skillima.screens.guild.GuildSelectionScreen
import skillima.screens.guild.GuildViewModel

@Composable
fun GuildScreen(
    navigator: Navigator = getKoin().get(),
    viewModel: GuildViewModel = koinViewModel(),

    ) {
    val fetchGuildState by viewModel.fetchGuildState.collectAsState()
    val selectedGuild by viewModel.selectedGuilds.collectAsState()
    val selectedSkills by viewModel.selectedSkills.collectAsState()
    val isFetching by viewModel.isFetching.collectAsStateWithLifecycle()
    val saveSkillsState by viewModel.saveSkillsState.collectAsState()
    val skillsState by viewModel.skillsState.collectAsState()

    GuildSelectionScreen(
        fetchedGuilds = fetchGuildState,
        onEvent = viewModel::onEvent,
        selectedGuilds = selectedGuild,
        selectedSkills = selectedSkills,
        isFetching  = isFetching,
        saveSkillsState = saveSkillsState,
        skillsState = skillsState
    )
}