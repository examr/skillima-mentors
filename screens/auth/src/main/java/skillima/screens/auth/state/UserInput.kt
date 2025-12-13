package skillima.screens.auth.state

import skillima.core.utils.validator.EmailValidator
import skillima.core.utils.validator.PasswordStrength
import skillima.core.utils.validator.PasswordValidationParameter
import skillima.core.utils.validator.PasswordValidator


data class UserInput(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordValidation: Map<PasswordValidationParameter, Boolean> = emptyMap(),
    val passwordStrength: PasswordStrength = PasswordStrength.WEAK,
    val isEmailValid: Boolean = false,
    val isNameValid: Boolean = false
) {

    fun updatePassword(newPassword: String) = copy(
        password = newPassword
    ).validate()

    fun updateName(newName: String) = copy(
        name = newName
    ).validate()

    fun updateEmail(newEmail: String) = copy(
        email = newEmail
    ).validate()

    private fun validate(): UserInput {
        val isValidEmail = EmailValidator.validateEmail(email)
        val isValidName = name.length >= 5

        val (validationResults, strength) =
            if (password.isNotBlank()) {
                PasswordValidator.isStrongPassword(password, email, name)
            } else {
                emptyMap<PasswordValidationParameter, Boolean>() to PasswordStrength.WEAK
            }

        return copy(
            isEmailValid = isValidEmail,
            isNameValid = isValidName,
            passwordValidation = validationResults,
            passwordStrength = strength
        )
    }

    fun clear() = UserInput()

    fun isSignupValid(): Boolean =
        isNameValid &&
                isEmailValid &&
                passwordStrength == PasswordStrength.STRONG

    fun isLoginValid(): Boolean =
        isEmailValid && password.isNotBlank() && password.length >=6

    companion object {
        fun createWithValidation(
            name: String = "",
            email: String = "",
            password: String = ""
        ): UserInput {
            return UserInput(
                name = name,
                email = email,
                password = password
            ).validate()
        }
    }
}
