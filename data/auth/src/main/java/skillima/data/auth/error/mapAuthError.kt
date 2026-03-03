package skillima.data.auth.error


fun mapAuthError(
    code: String?,
    message: String? = null
): AuthError {

    return when (code) {

        // --- Login / Signup common failures ---
        "invalid_credentials" -> AuthError.InvalidCredentials
        "user_not_found" -> AuthError.UserNotFound
        "email_exists" -> AuthError.EmailExists
        "email_not_confirmed" -> AuthError.EmailNotConfirmed
        "weak_password" -> AuthError.WeakPassword
        "same_password" -> AuthError.SamePassword

        // --- Request / rate limiting ---
        "over_request_rate_limit",
        "over_email_send_rate_limit",
        "over_sms_send_rate_limit" -> AuthError.TooManyRequests

        // --- Session / token issues ---
        "session_expired",
        "session_not_found",
        "refresh_token_not_found",
        "refresh_token_already_used" -> AuthError.SessionExpired

        // --- Generic conflicts ---
        "conflict" -> AuthError.Conflict

        // --- Provider / OAuth ---
        "oauth_provider_not_supported",
        "provider_disabled" -> AuthError.ProviderDisabled
        // --- Validation / bad inputs ---
        "validation_failed" -> AuthError.ValidationFailed
        "bad_json" -> AuthError.BadRequest
        "captcha_failed" -> AuthError.CaptchaFailed

        // --- Authorization issues ---
        "no_authorization",
        "not_admin" -> AuthError.NotAuthorized
        else -> AuthError.Unknown
    }
}

