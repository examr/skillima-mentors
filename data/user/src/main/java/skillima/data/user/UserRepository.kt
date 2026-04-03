package skillima.data.user

import kotlinx.coroutines.flow.Flow
import skillima.mentors.module.UserData
import skillima.mentors.utils.Response


interface UserRepository {
    fun createUserProfile(userData: UserData): Flow<Response<Boolean>>
    fun getUserProfile(): Flow<Response<UserData>>
}