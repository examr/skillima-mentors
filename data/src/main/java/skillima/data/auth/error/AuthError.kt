package skillima.data.auth.error

import skillima.core.supabase.SupabaseError
import skillima.data.R.string as str

sealed class AuthError(override val error: Int) : SupabaseError {

    // ---- General ----
    data object Unknown : AuthError(str.auth_error_unknown)
    data object Network : AuthError(str.auth_error_network)
    data object TooManyRequests : AuthError(str.auth_error_too_many_requests)
    data object Conflict : AuthError(str.auth_error_conflict)
    data object BadRequest : AuthError(str.auth_error_bad_request)

    // ---- Credentials / Login ----
    data object InvalidCredentials : AuthError(str.auth_error_invalid_credentials)
    data object UserNotFound : AuthError(str.auth_error_user_not_found)
    data object UserDisabled : AuthError(str.auth_error_user_disabled)

    // ---- Email ----
    data object EmailInvalid : AuthError(str.auth_error_email_invalid)
    data object EmailExists : AuthError(str.auth_error_email_exists)
    data object EmailNotConfirmed : AuthError(str.auth_error_email_not_confirmed)

    // ---- Password ----
    data object WeakPassword : AuthError(str.auth_error_weak_password)
    data object SamePassword : AuthError(str.auth_error_same_password)

    // ---- Session / Token ----
    data object SessionExpired : AuthError(str.auth_error_session_expired)
    data object NotAuthorized : AuthError(str.auth_error_not_authorized)

    // ---- Provider / OAuth ----
    data object ProviderDisabled : AuthError(str.auth_error_provider_disabled)
    data object ProviderEmailNeedsVerification :
        AuthError(str.auth_error_provider_email_needs_verification)

    // ---- Validation ----
    data object ValidationFailed : AuthError(str.auth_error_validation_failed)

    // ---- Captcha ----
    data object CaptchaFailed : AuthError(str.auth_error_captcha_failed)

    // ---- MFA ----
    data object MfaVerificationFailed : AuthError(str.auth_error_mfa_failed)
    data object MfaExpired : AuthError(str.auth_error_mfa_expired)
}
