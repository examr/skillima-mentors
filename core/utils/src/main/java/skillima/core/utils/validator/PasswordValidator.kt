
package skillima.core.utils.validator

object PasswordValidator {
    fun isStrongPassword(
        password: String,
        email: String,
        name: String,
    ): Pair<Map<PasswordValidationParameter, Boolean>, PasswordStrength> {
        val trimmedPassword = password.trim()
        val trimmedEmail = email.trim()
        val splitNames = name.trim().split(" ").filter { it.isNotEmpty() }

        val containsPersonalInfo = trimmedPassword.contains(trimmedEmail, ignoreCase = true) ||
                splitNames.any { n ->
                    trimmedPassword.contains(n, ignoreCase = true)
                }

        val validations = mutableMapOf<PasswordValidationParameter, Boolean>()

        val hasUppercase = trimmedPassword.any { it.isUpperCase() }
        validations[PasswordValidationParameter.HAS_UPPERCASE] = hasUppercase

        val hasLowercase = trimmedPassword.any { it.isLowerCase() }
        validations[PasswordValidationParameter.HAS_LOWERCASE] = hasLowercase

        val hasDigit = trimmedPassword.any { it.isDigit() }
        validations[PasswordValidationParameter.HAS_DIGITS] = hasDigit

        val hasSpecialChar = trimmedPassword.any { it in listOf('@', '$', '!', '%', '*', '?', '&') }
        validations[PasswordValidationParameter.HAS_SPECIAL_CHAR] = hasSpecialChar

        val hasMinLength = trimmedPassword.length >= 8
        validations[PasswordValidationParameter.HAS_LENGTH] = hasMinLength

        validations[PasswordValidationParameter.NOT_CONTAINING_PERSONAL_INFO] = !containsPersonalInfo

        val passedChecks = validations.values.count { it }

        val passwordStrength = when {
            passedChecks == validations.size -> PasswordStrength.STRONG
            passedChecks >= 4 -> PasswordStrength.MODERATE
            else -> PasswordStrength.WEAK
        }

        return Pair(validations, passwordStrength)
    }
}


enum class PasswordValidationParameter {
    HAS_UPPERCASE,
    HAS_LOWERCASE,
    HAS_DIGITS,
    HAS_SPECIAL_CHAR,
    HAS_LENGTH,
    NOT_CONTAINING_PERSONAL_INFO
}

enum class PasswordStrength {
    WEAK, MODERATE, STRONG
}
