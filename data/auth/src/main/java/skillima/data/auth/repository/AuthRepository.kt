package skillima.data.auth.repository

import kotlinx.coroutines.flow.Flow
import skillima.mentors.module.UserData
import skillima.mentors.utils.Response

interface AuthRepository {
     fun login(userData: UserData): Flow<Response<UserData>>
    fun signup(userData:UserData):Flow<Response<UserData>>
}