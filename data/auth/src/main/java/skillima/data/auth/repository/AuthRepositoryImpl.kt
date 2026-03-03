package skillima.data.auth.repository

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import skillima.mentors.module.UserData
import skillima.mentors.utils.Response
import skillima.data.auth.error.AuthError
import skillima.data.auth.error.mapAuthError

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    private val auth = supabaseClient.auth

    override fun login(userData: UserData ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            auth.signInWith(Email) {
                email = userData.email
                password = userData.password
            }

            emit(Response.Success(true))

        } catch (e: AuthRestException) {
            emit(Response.Error(mapAuthError(e.error)))

        } catch (e: Exception) {
            emit(Response.Error(AuthError.Unknown))
        }
    }.flowOn(Dispatchers.IO)

    override fun signup(userData: UserData): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            auth.signUpWith(Email) {
                email = userData.email
                password = userData.password
                data = buildJsonObject {
                    put("name", userData.name)
                }
            }

            emit(Response.Success(true))

        } catch (e: AuthRestException) {
            Log.e("TAG",e.error)
            emit(Response.Error(mapAuthError(e.error)))

        } catch (e: Exception) {

            emit(Response.Error(AuthError.Unknown))
        }
    }.flowOn(Dispatchers.IO)
}
