package skillima.screens.guild.model

data class Guild(
    val id: String,
    val name: String,
    val description: String?,
    val slug: String,
    val iconUrl: String?,
    val bannerUrl: String?,
    val memberCount: Int,
    val projectCount: Int,
    val skills: List<Skill>
)