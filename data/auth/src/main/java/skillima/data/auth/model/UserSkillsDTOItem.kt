package skillima.data.auth.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSkillsDTOItem(
    @SerialName("proficiency_level")
    val proficiencyLevel: String,
    @SerialName("skill_id")
    val skillId: String,
    @SerialName("user_id")
    val userId: String
)