package skillima.data.guild.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import skillima.mentors.module.SkillDto

@Serializable
data class SkillResponseDto(
    @SerialName("total_count")
    val totalCount: Long,
    val skills: List<SkillDto>
)