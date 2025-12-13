package skillima.data.auth.repository

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import skillima.core.module.UserData
import skillima.core.utils.Response
import skillima.data.auth.error.AuthError
import skillima.data.auth.error.mapAuthError
import kotlin.math.log

class AuthRepositoryImpl(private val supabaseClient: SupabaseClient): AuthRepository {
    val supabaseAuth = supabaseClient.auth


    override fun login(userData: UserData): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        runCatching {

            supabaseAuth.signInWith(Email) {
                email = userData.email
                password = userData.password


            }

            true

        }.onSuccess { result ->
            emit(Response.Success(result))

        }.onFailure { error ->

            val authError = when (error) {
                is AuthRestException -> mapAuthError(error.errorCode?.name)
                else -> AuthError.Unknown
            }

            emit(Response.Error(authError))
        }
    }


}