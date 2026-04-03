package skillima.data.local.repository

import kotlinx.coroutines.flow.Flow
import skillima.data.local.entity.UserEntity

interface UserLocalRepository {
    suspend fun saveUser(user: UserEntity)
    fun observeUser(): Flow<UserEntity?>
    suspend fun clearUser()
    suspend fun getUserId(): String?

}