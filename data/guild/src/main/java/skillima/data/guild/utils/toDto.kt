package skillima.data.guild.utils

import skillima.data.guild.model.GuildWithSkills
import skillima.data.guild.model.GuildSkillNetwork
import skillima.mentors.module.GuildDto
import skillima.mentors.module.GuildSkillDto

fun GuildWithSkills.toDto() = GuildDto(
    id = id,
    name = name,
    slug = slug,
    description = description,
    iconUrl = iconUrl,
    bannerUrl = bannerUrl,
    memberCount = memberCount,
    projectCount = projectCount,
    skills = skills.map { it.toDto() },
    totalCount = totalCount  // ← this line is likely missing
)
fun GuildSkillNetwork.toDto(): GuildSkillDto {
    return GuildSkillDto(
        id = id,
        name = name,
        slug = slug,
        iconUrl = iconUrl,
        userCount = userCount
    )
}