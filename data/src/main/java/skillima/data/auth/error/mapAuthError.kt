package skillima.data.auth.error

fun mapAuthError(
    code: String?,
    message: String? = null   // optional → powerful for future changes
): AuthError {

    return when (code) {
        "INVALID_CREDENTIALS" -> AuthError.InvalidCredentials
        "USER_NOT_FOUND" -> AuthError.UserNotFound
        "USER_DISABLED" -> AuthError.UserDisabled

        "EMAIL_EXISTS" -> AuthError.EmailExists
        "EMAIL_INVALID" -> AuthError.EmailInvalid
        "EMAIL_NOT_CONFIRMED" -> AuthError.EmailNotConfirmed

        "TOO_MANY_REQUESTS" -> AuthError.TooManyRequests
        "CONFLICT" -> AuthError.Conflict

        else -> AuthError.Unknown
    }
}
