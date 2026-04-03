package skillima.data.guild.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import skillima.mentors.module.SkillDto

@Serializable
data class SkillResponse(
    @SerialName("skills")
    val skills: List<SkillDto>,
    @SerialName("total_count")
    val totalCount: Long
)