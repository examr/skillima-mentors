package skillima.data.local.repository.skills

import kotlinx.coroutines.flow.Flow
import skillima.data.local.entity.UserSkillEntity

interface UserSkillLocalRepository {
    suspend fun saveSkills(skills: List<UserSkillEntity>)
    fun getSkills(): Flow<List<UserSkillEntity>>
    suspend fun clearSkills()
}