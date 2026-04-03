package skillima.mentors.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuildSkillDto(
    val id: String = "",
    val name: String = "",
    val slug: String = "",

    @SerialName("icon_url")
    val iconUrl: String? = null,

    @SerialName("user_count")
    val userCount: Int = 0
)