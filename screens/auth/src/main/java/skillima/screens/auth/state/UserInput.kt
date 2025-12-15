package skillima.screens.auth.state

import skillima.core.utils.validator.PasswordStrength
import skillima.core.utils.validator.PasswordValidationParameter

data class UserInput(
    val name: String = "",
    val email: String = "",
    val password: String = "",

    val passwordValidation: Map<PasswordValidationParameter, Boolean> = emptyMap(),
    val passwordStrength: PasswordStrength = PasswordStrength.WEAK,

    val isNameValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,

    val nameInteracted: Boolean = false,
    val emailInteracted: Boolean = false,
    val passwordInteracted: Boolean = false
) {



    fun updateName(newName: String): UserInput =
        copy(name = newName)

    fun updateEmail(newEmail: String): UserInput =
        copy(email = newEmail)

    fun updatePassword(newPassword: String): UserInput =
        copy(password = newPassword)


    fun applyNameValidation(isValid: Boolean): UserInput =
        copy(
            isNameValid = isValid,
            nameInteracted = name.isNotEmpty()
        )

    fun applyEmailValidation(isValid: Boolean): UserInput =
        copy(
            isEmailValid = isValid,
            emailInteracted = email.isNotEmpty()
        )

    fun applyPasswordValidation(
        isValid: Boolean,
        strength: PasswordStrength,
        rules: Map<PasswordValidationParameter, Boolean>
    ): UserInput =
        copy(
            isPasswordValid = isValid,
            passwordStrength = strength,
            passwordValidation = rules,
            passwordInteracted = password.isNotEmpty()
        )


    fun isSignupValid(): Boolean =
        isNameValid && isEmailValid && isPasswordValid

    fun isLoginValid(): Boolean =
        isEmailValid && password.length >= 6

    fun clear(): UserInput = UserInput()
}
