package skillima.data.auth.repository

import android.R.attr.password
import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import skillima.data.auth.error.AuthError
import skillima.data.auth.error.mapAuthError
import skillima.data.auth.model.AuthUserDTO
import skillima.data.auth.model.UserSkillsDTO
import skillima.data.auth.model.UserSkillsDTOItem
import skillima.data.auth.model.toAuthUser
import skillima.data.local.repository.local.LocalAppDataRepository
import skillima.mentors.module.AuthResult
import skillima.mentors.module.AuthUser
import skillima.mentors.module.UserData
import skillima.mentors.module.isMentorProfileComplete
import skillima.mentors.supabase.SupabaseConstants
import skillima.mentors.utils.Response
import kotlin.getOrDefault

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val localAppDataRepository: LocalAppDataRepository,
) : AuthRepository {

    private val auth = supabaseClient.auth

    override fun login(userData: UserData): Flow<Response<AuthResult>> = flow {
        emit(Response.Loading)
        try {
            auth.signInWith(Email) {
                email = userData.email
                password = userData.password
            }

            val results = auth.currentUserOrNull()

            val authUser = supabaseClient.from(SupabaseConstants.PROFILE).select{
                filter { eq("id", results?.id ?: "") }
            }.decodeSingle<AuthUserDTO>().toAuthUser()
            Log.i("TAG", "login: $authUser")
            val hasSkills = runCatching {
                val result = supabaseClient.from(SupabaseConstants.USER_SKILLS)
                    .select {
                        filter { eq("user_id", results?.id ?: "") }
                    }.decodeList<UserSkillsDTOItem>()
                (result.count()) > 0L
            }.getOrDefault(false)

            val isProfileComplete = authUser.isMentorProfileComplete()

            localAppDataRepository.setLoggedIn(true)
            localAppDataRepository.setProfileComplete(isProfileComplete)

            if (hasSkills) {
                localAppDataRepository.setGuildSelectionComplete(true)
            }
            emit(Response.Success(AuthResult(authUser, hasSkills, isProfileComplete)))
        } catch (e: AuthRestException) {
            Log.e("TAG", e.localizedMessage ?: "")
            emit(Response.Error(mapAuthError(e.error)))
        } catch (e: Exception) {
            Log.e("TAG", e.localizedMessage ?: "")
            emit(Response.Error(AuthError.Unknown))
        }
    }.flowOn(Dispatchers.IO)

    override fun signup(userData: UserData): Flow<Response<AuthResult>> = flow {
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

            auth.signInWith(Email) {
                this.email = userData.email
                this.password = userData.password
            }

            val result = supabaseClient.auth.currentUserOrNull()

            supabaseClient.from(SupabaseConstants.PROFILE).select {
                filter { eq("id", result?.id ?: "") }
            }.decodeSingle<AuthUserDTO>().toAuthUser()
        }.onSuccess { resolvedUser ->
            val isProfileComplete = resolvedUser.isMentorProfileComplete()
            Log.i("TAG", "signup: $resolvedUser $isProfileComplete")
            localAppDataRepository.setLoggedIn(true)
            localAppDataRepository.setProfileComplete(isProfileComplete)
            emit(Response.Success(AuthResult(resolvedUser, false, isProfileComplete)))
        }.onFailure { e ->
            Log.i("TAG", "signup: $e")
            when (e) {
                is AuthRestException -> emit(Response.Error(mapAuthError(e.error)))
                else -> emit(Response.Error(AuthError.Unknown))
            }
        }
    }.flowOn(Dispatchers.IO)
}
