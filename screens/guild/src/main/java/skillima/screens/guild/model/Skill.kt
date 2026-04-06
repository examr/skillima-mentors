package skillima.screens.guild.model

import skillima.data.local.entity.UserSkillEntity

data class Skill(
    val id: String,
    val name: String,
    val slug: String,
    val iconUrl: String?,
    val userCount: Int
)

fun Skill.toUserSkillEntity() = UserSkillEntity(
    id = id,
    name = name,
    slug = slug,
    iconUrl = iconUrl
)