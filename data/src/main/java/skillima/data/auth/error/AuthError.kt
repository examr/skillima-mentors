package skillima.data.auth.error

import skillima.core.supabase.SupabaseError
import skillima.data.R.string as str


sealed class AuthError(override val error: Int) : SupabaseError {


    data object Unknown : AuthError(str.auth_error_unknown)
    data object Network : AuthError(str.auth_error_network)
    data object TooManyRequests : AuthError(str.auth_error_too_many_requests)


    data object InvalidCredentials : AuthError(str.auth_error_invalid_credentials)
    data object UserNotFound : AuthError(str.auth_error_user_not_found)
    data object UserDisabled : AuthError(str.auth_error_user_disabled)


    data object EmailInvalid : AuthError(str.auth_error_email_invalid)
    data object EmailExists : AuthError(str.auth_error_email_exists)
    data object EmailNotConfirmed : AuthError(str.auth_error_email_not_confirmed)


    data object Conflict : AuthError(str.auth_error_conflict)
}

