package skillima.data.guild.repository

import kotlinx.coroutines.flow.Flow
import skillima.data.guild.model.GuildWithSkills
import skillima.mentors.module.GuildDto
import skillima.mentors.module.SkillDto
import skillima.mentors.utils.Response
interface GuildRepository {
    suspend fun fetchGuilds(offset: Long, limit: Long): Flow<Response<List<GuildDto>>>
    suspend fun saveUserSkills(
        userId: String,
        skillIds: List<String>,
        proficiency: String = "beginner"
    ): Flow<Response<Boolean>>

    suspend fun searchGuilds(
        search: String,
        offset: Long,
        limit: Long
    ): Flow<Response<List<GuildDto>>>

    suspend fun searchSkills(
        search: String,
        offset: Long,
        limit: Long
    ): Flow<Response<List<SkillDto>>>

    suspend fun fetchSkills(offset: Long, limit: Long): Flow<Response<List<SkillDto>>>
}