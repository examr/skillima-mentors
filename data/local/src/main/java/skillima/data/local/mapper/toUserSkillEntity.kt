package skillima.data.local.mapper

import skillima.data.local.entity.UserSkillEntity
import skillima.mentors.module.SkillDto

fun SkillDto.toUserSkillEntity() = UserSkillEntity(
    id = id,
    name = name,
    slug = slug,
    iconUrl = iconUrl
)

fun UserSkillEntity.toDto() = SkillDto(
    id = id,
    name = name,
    slug = slug,
    iconUrl = iconUrl
)