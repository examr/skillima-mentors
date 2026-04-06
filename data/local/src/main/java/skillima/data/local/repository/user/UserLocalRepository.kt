package skillima.data.local.repository.user

import kotlinx.coroutines.flow.Flow
import skillima.data.local.entity.UserEntity
import skillima.data.local.entity.UserSkillEntity

interface UserLocalRepository {
    suspend fun saveUser(user: UserEntity)
    fun observeUser(): Flow<UserEntity?>
    suspend fun clearUser()
    suspend fun getUserId(): String?

    suspend fun saveUserSkills(skills: List<UserSkillEntity>)
    fun getUserSkills(): Flow<List<UserSkillEntity>>
    suspend fun clearUserSkills()

}