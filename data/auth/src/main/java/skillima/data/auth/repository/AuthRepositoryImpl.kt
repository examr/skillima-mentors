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
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import skillima.mentors.module.UserData
import skillima.mentors.utils.Response
import skillima.data.auth.error.AuthError
import skillima.data.auth.error.mapAuthError
import skillima.data.local.repository.LocalAppDataRepository

class AuthRepositoryImpl(
    supabaseClient: SupabaseClient,
    private val localAppDataRepository: LocalAppDataRepository
) : AuthRepository {

    private val auth = supabaseClient.auth

    override fun login(userData: UserData): Flow<Response<UserData>> = flow {
        emit(Response.Loading)
        try {
            auth.signInWith(Email) {
                email = userData.email
                password = userData.password
            }

            val session = auth.currentUserOrNull()
            val resolvedUser = userData.copy(
                userId = session?.id ?: "",
                name = session?.userMetadata?.get("name")?.jsonPrimitive?.content ?: userData.name
            )

            localAppDataRepository.setLoggedIn(true)
            emit(Response.Success(resolvedUser))
        } catch (e: AuthRestException) {
            Log.e("TAG", e.localizedMessage ?: "")
            emit(Response.Error(mapAuthError(e.error)))
        } catch (e: Exception) {
            Log.e("TAG", e.localizedMessage ?: "")
            emit(Response.Error(AuthError.Unknown))
        }
    }.flowOn(Dispatchers.IO)

    override fun signup(userData: UserData): Flow<Response<UserData>> = flow {
        emit(Response.Loading)
        runCatching {
            auth.signUpWith(Email) {
                email = userData.email
                password = userData.password
                data = buildJsonObject {
                    put("name", userData.name)
                    put("role", "mentor")
                }
            }
        }.onSuccess {
            auth.signInWith(Email) {
                this.email = userData.email
                this.password = userData.password
            }

            val session = auth.currentUserOrNull()
            val resolvedUser = userData.copy(
                userId = session?.id ?: "",
                name = session?.userMetadata?.get("name")?.jsonPrimitive?.content ?: userData.name
            )

            localAppDataRepository.setLoggedIn(true)
            emit(Response.Success(resolvedUser))
        }.onFailure { e ->
            when (e) {
                is AuthRestException -> emit(Response.Error(mapAuthError(e.error)))
                else -> emit(Response.Error(AuthError.Unknown))
            }
        }
    }.flowOn(Dispatchers.IO)
}