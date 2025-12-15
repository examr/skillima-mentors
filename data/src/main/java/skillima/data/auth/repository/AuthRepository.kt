package skillima.data.auth.repository

import kotlinx.coroutines.flow.Flow
import skillima.core.module.UserData
import skillima.core.utils.Response

interface AuthRepository {
     fun login(userData: UserData): Flow<Response<Boolean>>
    fun signup(userData:UserData):Flow<Response<Boolean>>
}