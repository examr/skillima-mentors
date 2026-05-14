package skillima.mentors.module

/**
 * Unified result returned by [AuthRepository.login] and [AuthRepository.signup].
 *
 * @param user          The authenticated user's profile data.
 * @param hasSkills     True when the user has at least one skill selected (guild selection done).
 * @param isProfileComplete  True when the mentor has filled in all required profile fields.
 */
data class AuthResult(
    val user: AuthUser,
    val hasSkills: Boolean,
    val isProfileComplete: Boolean,
)
