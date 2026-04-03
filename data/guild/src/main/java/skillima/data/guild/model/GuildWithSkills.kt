package skillima.data.guild.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class GuildWithSkills(
    val id: String,
    val name: String,
    val slug: String,
    val description: String? = null,

    @SerialName("icon_url")
    val iconUrl: String? = null,

    @SerialName("banner_url")
    val bannerUrl: String? = null,

    @SerialName("member_count")
    val memberCount: Int = 0,

    @SerialName("project_count")
    val projectCount: Int = 0,

    val skills: List<GuildSkillNetwork> = emptyList(),

    @SerialName("total_count")
    val totalCount: Long = 0L
)

@Serializable
data class GuildSkillNetwork(
    val id: String,
    val name: String,
    val slug: String,

    @SerialName("icon_url")
    val iconUrl: String? = null,

    @SerialName("user_count")
    val userCount: Int = 0
)