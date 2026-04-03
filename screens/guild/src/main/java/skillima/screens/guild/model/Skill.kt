package skillima.screens.guild.model

data class Skill(
    val id: String,
    val name: String,
    val slug: String,
    val iconUrl: String?,
    val userCount: Int
)