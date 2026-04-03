package skillima.screens.guild

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import skillima.screens.guild.model.FetchGuildState
import skillima.screens.guild.model.GuildEvents
import skillima.screens.guild.model.Guild
import skillima.screens.guild.model.SaveSkillsState
import skillima.screens.guild.model.Skill
import skillma.core.ui.design.button.SkillimaButton
import skillma.core.ui.design.input.SkillimaTextField
import skillma.core.ui.design.logo.SkillimaLogo
import skillma.core.ui.design.utils.ButtonColor
import skillma.core.ui.design.utils.ButtonState
import skillma.core.ui.theme.NeutralBlack12
import skillma.core.ui.theme.NeutralBlack5
import skillma.core.ui.theme.NeutralBlack9

private enum class ScreenMode { GUILD_SELECTION, SKILL_SELECTION }
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GuildSelectionScreen(
    fetchedGuilds: FetchGuildState,
    skillsState: List<Skill>, // ADDED
    selectedGuilds: Set<String>,
    selectedSkills: Set<String>,
    onEvent: (GuildEvents) -> Unit,
    isFetching: Boolean,
    saveSkillsState: SaveSkillsState
) {
    var buttonState by remember { mutableStateOf<ButtonState>(ButtonState.Idle) }
    var search by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isFetchingMore by remember { mutableStateOf(false) }
    var guilds by remember { mutableStateOf<List<Guild>>(emptyList()) }
    var screenMode by remember { mutableStateOf(ScreenMode.GUILD_SELECTION) }

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val isJourneyEnabled = selectedGuilds.isNotEmpty() || selectedSkills.isNotEmpty()
    val isSaving = saveSkillsState is SaveSkillsState.Loading

    BackHandler(enabled = screenMode == ScreenMode.SKILL_SELECTION) {
        screenMode = ScreenMode.GUILD_SELECTION
        search = ""
    }

    LaunchedEffect(Unit) {
        onEvent(GuildEvents.FetchGuild(6))
    }

    LaunchedEffect(saveSkillsState) {
        when (saveSkillsState) {
            is SaveSkillsState.Error -> {
                buttonState = ButtonState.Idle
                val message = context.getString(saveSkillsState.errorMessage)
                snackBarHostState.showSnackbar(message)
            }
            is SaveSkillsState.Success -> buttonState = ButtonState.Success
            is SaveSkillsState.Loading -> buttonState = ButtonState.Loading
            SaveSkillsState.Idle -> Unit
        }
    }

    LaunchedEffect(fetchedGuilds) {
        when (fetchedGuilds) {
            is FetchGuildState.Error -> {
                isFetchingMore = false
                isLoading = false
                val message = context.getString(fetchedGuilds.errorMessage)
                snackBarHostState.showSnackbar(message)
            }
            FetchGuildState.Idle -> {
                isLoading = false
                isFetchingMore = false
            }
            FetchGuildState.Loading -> {
                if (guilds.isEmpty()) isLoading = true
                else isFetchingMore = true
            }
            is FetchGuildState.Success -> {
                isLoading = false
                isFetchingMore = false
                guilds = fetchedGuilds.data
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) },
            containerColor = MaterialTheme.colorScheme.background,
            topBar = { /* KEEP YOUR TOP BAR SAME */ },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (screenMode == ScreenMode.GUILD_SELECTION) {
                        Text(
                            text = "no, i'll choose my own",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable(enabled = !isSaving) {
                                screenMode = ScreenMode.SKILL_SELECTION
                                search = ""
                                onEvent(GuildEvents.FetchSkills(20)) // IMPORTANT
                            }
                        )
                    }

                    SkillimaButton(
                        state = buttonState,
                        onClick = {
                            if (isJourneyEnabled && !isSaving) {
                                onEvent(GuildEvents.SaveSkills)
                            }
                        },
                        enabled = isJourneyEnabled && buttonState == ButtonState.Idle,
                        content = { Text("Start Journey") },
                        colors = ButtonColor.Primary,
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    )
                }
            }
        ) { padding ->
            AnimatedContent(
                targetState = screenMode,
                label = "screen_transition"
            ) { mode ->
                when (mode) {
                    ScreenMode.GUILD_SELECTION -> {
                        GuildSelectionContent(
                            padding = padding,
                            search = search,
                            onSearchChange = {
                                search = it
                                onEvent(GuildEvents.SearchGuild(it)) // UPDATED
                            },
                            isLoading = isLoading,
                            isFetchingMore = isFetchingMore,
                            isFetching = isFetching,
                            onLoadMore = { onEvent(GuildEvents.FetchGuild(5)) },
                            guilds = guilds, // REMOVED LOCAL FILTER
                            selectedGuilds = selectedGuilds,
                            onGuildToggle = { onEvent(GuildEvents.ToggleGuild(it)) }
                        )
                    }

                    ScreenMode.SKILL_SELECTION -> {
                        SkillSelectionContent(
                            padding = padding,
                            search = search,
                            onSearchChange = {
                                search = it
                                onEvent(GuildEvents.SearchSkills(it))
                            },
                            skills = skillsState,
                            selectedSkills = selectedSkills,
                            onSkillToggle = { onEvent(GuildEvents.ToggleSkill(it)) },
                            onLoadMore = { onEvent(GuildEvents.FetchSkills(20)) },
                            isLoading = skillsState.isEmpty() && isLoading,
                            onBackClick = {
                                screenMode = ScreenMode.GUILD_SELECTION
                                search = ""
                            }
                        )
                    }
                }
            }
        }
    }
}


// ── Guild Selection Content ──────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun GuildSelectionContent(
    padding: PaddingValues,
    search: String,
    onSearchChange: (String) -> Unit,
    isLoading: Boolean,
    isFetchingMore: Boolean,
    isFetching: Boolean,
    onLoadMore: () -> Unit,
    guilds: List<Guild>,
    selectedGuilds: Set<String>,
    onGuildToggle: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "About You",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "one last step before exploring product, let's know you better",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        SkillimaTextField(
            modifier = Modifier.fillMaxWidth(),
            value = search,
            onValueChange = onSearchChange,
            hintValue = "Search",
            navigationIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "",
                    tint = NeutralBlack9
                )
            }
        )

        if (isLoading) {
            CircularWavyProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally),
                trackColor = NeutralBlack12,
            )
        } else {
            val listState = rememberLazyListState()

            val isAtEnd by remember {
                derivedStateOf {
                    val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    val total = listState.layoutInfo.totalItemsCount
                    total > 0 && lastVisible >= total - 2
                }
            }

            LaunchedEffect(isAtEnd) {
                if (isAtEnd && !isFetching) onLoadMore()
            }

            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(guilds, key = { it.id }) { guild ->
                    GuildCard(
                        guild = guild,
                        isSelected = selectedGuilds.contains(guild.id),
                        isDisabled = selectedGuilds.size >= 2 && !selectedGuilds.contains(guild.id),
                        onToggle = { onGuildToggle(guild.id) }
                    )
                }

                if (isFetchingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularWavyProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                trackColor = NeutralBlack12
                            )
                        }
                    }
                }
            }
        }
    }
}


// ── Skill Selection Content ──────────────────────────────────────────────────
@Composable
private fun SkillSelectionContent(
    padding: PaddingValues,
    search: String,
    onSearchChange: (String) -> Unit,
    skills: List<Skill>,
    selectedSkills: Set<String>,
    onSkillToggle: (String) -> Unit,
    onLoadMore: () -> Unit,
    isLoading: Boolean,
    onBackClick: () -> Unit
) {
    val gridState = rememberLazyGridState()

    val isAtEnd by remember {
        derivedStateOf {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = gridState.layoutInfo.totalItemsCount
            total > 0 && lastVisible >= total - 4
        }
    }

    LaunchedEffect(isAtEnd) {
        if (isAtEnd) onLoadMore()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // BARE BACK ARROW
        Icon(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "Back",
            modifier = Modifier
                .padding(top = 8.dp)
                .size(22.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onBackClick() },
            tint = MaterialTheme.colorScheme.onBackground
        )
        // TITLE + SUBTITLE
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Pick Your Skills",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Select the skills that match your expertise",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        // SEARCH
        SkillimaTextField(
            modifier = Modifier.fillMaxWidth(),
            value = search,
            onValueChange = onSearchChange,
            hintValue = "Search skills",
            navigationIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "",
                    tint = NeutralBlack9
                )
            }
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = skills,
                    key = { it.id }
                ) { skill ->
                    SkillGridItem(
                        skill = skill,
                        isSelected = selectedSkills.contains(skill.id),
                        onToggle = { onSkillToggle(skill.id) }
                    )
                }
            }
        }
    }
}


// ── Skill Grid Item ──────────────────────────────────────────────────────────

@Composable
fun SkillGridItem(
    skill: Skill,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(NeutralBlack12)
            .then(
                if (isSelected) Modifier.border(
                    1.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(8.dp)
                ) else Modifier
            )
            .clickable { onToggle() }
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(NeutralBlack5),
            contentAlignment = Alignment.Center
        ) {
            if (!skill.iconUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = skill.iconUrl,
                    contentDescription = skill.name,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    text = skill.name.first().toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Text(
            text = skill.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            painter = painterResource(if (isSelected) R.drawable.ic_tick else R.drawable.ic_plus),
            contentDescription = if (isSelected) "Selected" else "Add",
            modifier = Modifier.size(16.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary else NeutralBlack9
        )
    }
}


// ── GuildCard ────────────────────────────────────────────────────────────────

@Composable
fun GuildCard(
    guild: Guild,
    isSelected: Boolean,
    isDisabled: Boolean,
    onToggle: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isDisabled) NeutralBlack12.copy(alpha = 0.4f) else NeutralBlack12
            )
            .then(
                if (isSelected) Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                ) else Modifier
            )
            .clickable(enabled = !isDisabled) { onToggle() }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = guild.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = when {
                    isDisabled -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onBackground
                }
            )

            val rotation by animateFloatAsState(
                targetValue = if (expanded) 180f else 0f,
                animationSpec = tween(durationMillis = 300),
                label = "arrow_rotation"
            )

            Icon(
                painter = painterResource(R.drawable.ic_down_arrow),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(rotation)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { expanded = !expanded },
                tint = when {
                    isDisabled -> NeutralBlack9.copy(alpha = 0.3f)
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> NeutralBlack9
                }
            )
        }

        HorizontalDivider(
            thickness = Dp.Hairline,
            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            else NeutralBlack9
        )

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                guild.skills.forEach { skill -> SkillChip(skill = skill) }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OverlappingSkills(skills = guild.skills)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${guild.memberCount} People Chose this",
                style = MaterialTheme.typography.labelSmall,
                color = NeutralBlack9,
                fontSize = 12.sp
            )
        }
    }
}


@Composable
fun SkillChip(skill: Skill) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(NeutralBlack9)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (!skill.iconUrl.isNullOrEmpty()) {
            AsyncImage(
                model = skill.iconUrl,
                contentDescription = skill.name,
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = skill.name,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun OverlappingSkills(skills: List<Skill>, maxVisible: Int = 6) {
    val visibleSkills = skills.take(maxVisible)
    val remaining = skills.size - maxVisible

    Row {
        visibleSkills.forEachIndexed { index, skill ->
            Box(
                modifier = Modifier
                    .offset(x = (-8 * index).dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(NeutralBlack5)
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (!skill.iconUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = skill.iconUrl,
                        contentDescription = skill.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = skill.name.first().toString(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        if (remaining > 0) {
            Box(
                modifier = Modifier
                    .offset(x = (-8 * visibleSkills.size).dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(NeutralBlack9),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$remaining",
                    fontSize = 9.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}