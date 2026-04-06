package skillima.screens.guild


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import skillima.data.guild.repository.GuildRepository
import skillima.data.local.repository.skills.UserSkillLocalRepository
import skillima.data.local.repository.skills.UserSkillLocalRepositoryImpl
import skillima.data.local.repository.user.UserLocalRepository
import skillima.mentors.utils.Response
import skillima.screens.guild.model.FetchGuildState
import skillima.screens.guild.model.GuildEvents
import skillima.screens.guild.model.SaveSkillsState
import skillima.screens.guild.model.Skill
import skillima.screens.guild.model.toUserSkillEntity
import skillima.screens.guild.utils.toDomain

class GuildViewModel(
    private val guildRepository: GuildRepository,
    private val userLocalRepository: UserLocalRepository,
    private val userSkillLocalRepository: UserSkillLocalRepository
) : ViewModel() {

    private val _fetchGuildState = MutableStateFlow<FetchGuildState>(FetchGuildState.Idle)
    val fetchGuildState = _fetchGuildState.asStateFlow()

    private val _skillsState = MutableStateFlow<List<Skill>>(emptyList())
    val skillsState = _skillsState.asStateFlow()

    private val _selectedGuilds = MutableStateFlow<Set<String>>(emptySet())
    val selectedGuilds = _selectedGuilds.asStateFlow()

    private val _selectedSkills = MutableStateFlow<Set<String>>(emptySet())
    val selectedSkills = _selectedSkills.asStateFlow()

    private val _isFetching = MutableStateFlow(false)
    val isFetching = _isFetching.asStateFlow()

    private val _saveSkillsState = MutableStateFlow<SaveSkillsState>(SaveSkillsState.Idle)
    val saveSkillsState = _saveSkillsState.asStateFlow()

    private var guildSearchQuery: String = ""
    private var skillSearchQuery: String = ""

    private var currentOffset: Long = 0
    private var totalCount: Long = 0
    private var isLastPage = false

    private var skillOffset: Long = 0
    private var isSkillLastPage = false
    private var isSkillFetching = false
    private var currentPageSize: Long = 6

    fun onEvent(event: GuildEvents) {
        when (event) {
            is GuildEvents.FetchGuild -> {
                currentPageSize = event.pageSize
                if (guildSearchQuery.isEmpty()) fetchGuild()
                else searchGuild()
            }

            is GuildEvents.SearchGuild -> {
                guildSearchQuery = event.query
                resetGuildPagination()
                if (guildSearchQuery.isEmpty()) fetchGuild()
                else searchGuild()
            }

            is GuildEvents.FetchSkills -> {
                currentPageSize = event.pageSize
                if (skillSearchQuery.isEmpty()) fetchSkills()
                else searchSkills()
            }

            is GuildEvents.SearchSkills -> {
                skillSearchQuery = event.query
                resetSkillPagination()
                if (skillSearchQuery.isEmpty()) fetchSkills()
                else searchSkills()
            }

            is GuildEvents.ToggleGuild -> {
                val current = _selectedGuilds.value
                if (current.contains(event.guildId)) {
                    _selectedGuilds.value = current - event.guildId
                } else {
                    if (current.size < 2) {
                        _selectedGuilds.value = current + event.guildId
                        _selectedSkills.value = emptySet()
                    }
                }
            }

            is GuildEvents.ToggleSkill -> {
                val current = _selectedSkills.value
                if (current.contains(event.skillId)) {
                    _selectedSkills.value = current - event.skillId
                } else {
                    _selectedSkills.value = current + event.skillId
                    _selectedGuilds.value = emptySet()
                }
            }

            is GuildEvents.SaveSkills -> saveUserSkills()
        }
    }


    private fun resetGuildPagination() {
        currentOffset = 0
        totalCount = 0
        isLastPage = false
        _isFetching.value = false
        _fetchGuildState.value = FetchGuildState.Idle
    }

    private fun fetchGuild() {
        if (_isFetching.value || isLastPage) return

        viewModelScope.launch {
            _isFetching.value = true

            guildRepository.fetchGuilds(
                offset = currentOffset,
                limit = currentPageSize
            ).collect { res ->
                when (res) {
                    is Response.Loading -> {
                        if (currentOffset == 0L)
                            _fetchGuildState.value = FetchGuildState.Loading
                    }

                    is Response.Error -> {
                        _fetchGuildState.value = FetchGuildState.Error(res.exception.error)
                        _isFetching.value = false
                    }

                    is Response.Success -> {
                        totalCount = res.data.firstOrNull()?.totalCount ?: 0L
                        val newGuilds = res.data.toDomain()
                        val existing = (_fetchGuildState.value as? FetchGuildState.Success)?.data
                            ?: emptyList()


                        _fetchGuildState.value = FetchGuildState.Success(
                            (existing + newGuilds).distinctBy { it.id }
                        )

                        currentOffset += newGuilds.size
                        isLastPage = currentOffset >= totalCount
                        _isFetching.value = false
                    }
                }
            }
        }
    }

    private fun searchGuild() {
        if (_isFetching.value || isLastPage) return

        viewModelScope.launch {
            _isFetching.value = true

            guildRepository.searchGuilds(
                search = guildSearchQuery,
                offset = currentOffset,
                limit = currentPageSize
            ).collect { res ->
                when (res) {
                    is Response.Success -> {
                        totalCount = res.data.firstOrNull()?.totalCount ?: 0L
                        val newGuilds = res.data.toDomain()
                        val existing = (_fetchGuildState.value as? FetchGuildState.Success)?.data
                            ?: emptyList()


                        _fetchGuildState.value = FetchGuildState.Success(
                            (existing + newGuilds).distinctBy { it.id }
                        )

                        currentOffset += newGuilds.size
                        isLastPage = currentOffset >= totalCount
                        _isFetching.value = false
                    }

                    is Response.Error -> {
                        _fetchGuildState.value = FetchGuildState.Error(res.exception.error)
                        _isFetching.value = false
                    }

                    else -> {}
                }
            }
        }
    }


    private fun resetSkillPagination() {
        skillOffset = 0
        isSkillLastPage = false
        isSkillFetching = false
        _skillsState.value = emptyList()
    }

    private fun fetchSkills() {
        if (isSkillFetching || isSkillLastPage) return

        viewModelScope.launch {
            isSkillFetching = true
            guildRepository.fetchSkills(
                offset = skillOffset,
                limit = currentPageSize
            ).collect { res ->
                when (res) {
                    is Response.Success -> {
                        val newSkills = res.data.map { it.toDomain() }
                        _skillsState.value = (_skillsState.value + newSkills).distinctBy { it.id }
                        skillOffset += newSkills.size
                        if (newSkills.size < currentPageSize) isSkillLastPage = true
                        isSkillFetching = false
                    }

                    is Response.Error -> {
                        isSkillFetching = false
                    }

                    else -> {}
                }
            }
        }
    }

    private fun searchSkills() {
        if (isSkillFetching || isSkillLastPage) return
        viewModelScope.launch {
            isSkillFetching = true
            guildRepository.searchSkills(
                search = skillSearchQuery,
                offset = skillOffset,
                limit = currentPageSize
            ).collect { res ->
                when (res) {
                    is Response.Success -> {
                        val newSkills = res.data.map { it.toDomain() }
                        _skillsState.value = (_skillsState.value + newSkills).distinctBy { it.id }
                        skillOffset += newSkills.size
                        if (newSkills.size < currentPageSize) isSkillLastPage = true
                        isSkillFetching = false
                    }

                    is Response.Error -> {
                        isSkillFetching = false
                    }

                    else -> {}
                }
            }
        }
    }


    private fun saveUserSkills() {
        val guilds = (_fetchGuildState.value as? FetchGuildState.Success)?.data ?: emptyList()

        val skillIdsFromGuilds = guilds
            .filter { it.id in _selectedGuilds.value }
            .flatMap { guild -> guild.skills.map { it.id } }

        val skillIdsFromSkills = _selectedSkills.value.toList()
        val skillIds = (skillIdsFromGuilds + skillIdsFromSkills).distinct()

        if (skillIds.isEmpty()) return

        viewModelScope.launch {
            val userId = userLocalRepository.getUserId()

            if (userId == null) {
                _saveSkillsState.value = SaveSkillsState.Error(R.string.error_user_not_found)
                return@launch
            }

            guildRepository.saveUserSkills(
                userId = userId,
                skillIds = skillIds,
                proficiency = "beginner"
            ).collect { response ->
                when (response) {
                    is Response.Loading -> _saveSkillsState.value = SaveSkillsState.Loading

                    is Response.Success -> {
                        val skillsFromGuilds = guilds
                            .filter { it.id in _selectedGuilds.value }
                            .flatMap { it.skills }

                        val skillsFromSkills = _skillsState.value
                            .filter { it.id in _selectedSkills.value }

                        val allSkills = (skillsFromGuilds + skillsFromSkills)
                            .distinctBy { it.id }
                            .map { it.toUserSkillEntity() }

                        // Save to Room after Supabase confirms
                        userSkillLocalRepository.saveSkills(allSkills)

                        _saveSkillsState.value = SaveSkillsState.Success
                    }

                    is Response.Error -> _saveSkillsState.value =
                        SaveSkillsState.Error(response.exception.error)
                }
            }
        }
    }
}