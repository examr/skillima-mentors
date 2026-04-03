package skillima.data.guild.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuildResponse(
    val guilds: List<GuildWithSkills>,
    @SerialName("total_count")
    val totalCount: Long = 0L
)