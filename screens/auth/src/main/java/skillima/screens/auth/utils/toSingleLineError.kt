package skillima.screens.auth.utils

import skillima.core.utils.validator.PasswordValidationParameter

fun Map<PasswordValidationParameter, Boolean>.toSingleLineError(): String {
    if (isEmpty()) return ""

    return when {
        this[PasswordValidationParameter.HAS_LENGTH] == false ->
            "Password must be at least 8 characters long"

        this[PasswordValidationParameter.HAS_UPPERCASE] == false ->
            "Password must contain at least one uppercase letter"

        this[PasswordValidationParameter.HAS_LOWERCASE] == false ->
            "Password must contain at least one lowercase letter"

        this[PasswordValidationParameter.HAS_DIGITS] == false ->
            "Password must contain at least one number"

        this[PasswordValidationParameter.HAS_SPECIAL_CHAR] == false ->
            "Password must contain at least one special character"

        this[PasswordValidationParameter.NOT_CONTAINING_PERSONAL_INFO] == false ->
            "Password should not contain your name or email"

        else -> ""
    }
}
