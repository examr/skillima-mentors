package skillima.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import skillima.data.local.entity.UserSkillEntity

@Dao
interface UserSkillDao {

    @Query("SELECT * FROM user_skills")
    fun getUserSkills(): Flow<List<UserSkillEntity>>

    @Upsert
    suspend fun saveSkills(skills: List<UserSkillEntity>)

    @Query("DELETE FROM user_skills")
    suspend fun clearAll()
}