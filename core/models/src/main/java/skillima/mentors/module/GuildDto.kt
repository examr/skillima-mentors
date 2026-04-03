package skillima.mentors.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class GuildDto(
    val id: String = "",
    val name: String = "",
    val description: String? = null,
    val slug: String = "",

    @SerialName("icon_url")
    val iconUrl: String? = null,

    @SerialName("banner_url")
    val bannerUrl: String? = null,

    @SerialName("member_count")
    val memberCount: Int = 0,

    @SerialName("project_count")
    val projectCount: Int = 0,

    val skills: List<GuildSkillDto> = emptyList(),

    @SerialName("total_count")
    val totalCount: Long = 0L   // ← only change
)