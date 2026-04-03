package skillima.data.local.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import skillima.data.local.dao.UserDao
import skillima.data.local.entity.UserEntity

class UserLocalRepositoryImpl(
    private val userDao: UserDao
) : UserLocalRepository {

    override suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override fun observeUser(): Flow<UserEntity?> =
        userDao.getUser()

    override suspend fun clearUser() {
        userDao.clear()
    }
    override suspend fun getUserId(): String? {
        return userDao.getUser().firstOrNull()?.id
    }
}
