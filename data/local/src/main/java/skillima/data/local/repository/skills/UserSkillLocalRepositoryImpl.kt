
package skillima.data.local.repository.skills

import kotlinx.coroutines.flow.Flow
import skillima.data.local.dao.UserSkillDao
import skillima.data.local.entity.UserSkillEntity

class UserSkillLocalRepositoryImpl(
    private val userSkillDao: UserSkillDao
) : UserSkillLocalRepository {

    override suspend fun saveSkills(skills: List<UserSkillEntity>) {
        userSkillDao.saveSkills(skills)
    }

    override fun getSkills(): Flow<List<UserSkillEntity>> =
        userSkillDao.getUserSkills()

    override suspend fun clearSkills() {
        userSkillDao.clearAll()
    }
}