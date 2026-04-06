package skillima.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import skillima.data.local.entity.UserEntity
import skillima.data.local.entity.UserSkillEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Query("DELETE FROM user")
    suspend fun clear()

    @Upsert
    suspend fun saveUserSkills(skills: List<UserSkillEntity>)

    @Query("SELECT * FROM user_skills")
    fun getUserSkills(): Flow<List<UserSkillEntity>>

    @Query("DELETE FROM user_skills")
    suspend fun clearUserSkills()
}
