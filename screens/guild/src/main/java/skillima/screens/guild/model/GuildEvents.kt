package skillima.screens.guild.model

sealed class GuildEvents {

    // Guild
    data class FetchGuild(val pageSize: Long) : GuildEvents()
    data class SearchGuild(val query: String) : GuildEvents()

    // Skills
    data class FetchSkills(val pageSize: Long) : GuildEvents()
    data class SearchSkills(val query: String) : GuildEvents()

    // Selection
    data class ToggleGuild(val guildId: String) : GuildEvents()
    data class ToggleSkill(val skillId: String) : GuildEvents()

    // Save
    data object SaveSkills : GuildEvents()
}