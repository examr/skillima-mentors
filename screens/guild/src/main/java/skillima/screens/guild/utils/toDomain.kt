package skillima.screens.guild.utils

import skillima.mentors.module.GuildDto
import skillima.mentors.module.GuildSkillDto
import skillima.mentors.module.SkillDto
import skillima.screens.guild.model.Guild
import skillima.screens.guild.model.Skill

fun GuildDto.toDomain(): Guild {
    return Guild(
        id = id,
        name = name,
        description = description,
        slug = slug,
        iconUrl = iconUrl,
        bannerUrl = bannerUrl,
        memberCount = memberCount,
        projectCount = projectCount,
        skills = skills.map { it.toDomain() }
    )
}


fun SkillDto.toDomain(): Skill {
    return Skill(
        id = id,
        name = name,
        slug = slug,
        iconUrl = iconUrl,
        userCount = userCount
    )
}
fun GuildSkillDto.toDomain(): Skill {
    return Skill(
        id = id,
        name = name,
        slug = slug,
        iconUrl = iconUrl,
        userCount = userCount
    )
}

fun List<GuildDto>.toDomain(): List<Guild> {
    return map { it.toDomain() }
}